package colony.webproj.sse;

import colony.webproj.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;

    //로그인하면 js 에서 /subscribe/{id} 로 요청을 보내고 이 함수 실행 됨
    //sseEmitter 를 repo 에 저장
    //더미데이터 발송
    //미수신한 event 가 있다면
    public SseEmitter subscribe(Long userId, String lastEventId) {
        //memberId 와 현재 시간을 결합해서 id 생성
        String id = userId + "_" + System.currentTimeMillis();

        //SseEmitter 저장
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        //알림이 가거나 완료되면 제거
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        //503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if(!lastEventId.isEmpty()) {
            log.info("미수신 event 존재");
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0) //compareTo 문자 비교를 이용해서 값이 작으면 음수 리턴
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue())); //본인의 에미터, 이벤트 키, 이벤트 내용
        }
        log.info("emitter 반환");
        return emitter;
    }

    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            log.info("sendToClient: 알림 전달");
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
    }

    public void send(Member receiver, Long postId, String content) {
        log.info("send: 알림 전달");
        Notification notification = Notification.builder()
                .receiver(receiver)
                .content(content)
                .url("/post/" + postId)
                .isRead(false)
                .build();
        String id = String.valueOf(receiver.getId());

        //받을 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, notification);
                });
    }
}
