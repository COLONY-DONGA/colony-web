package colony.webproj.sse;

import colony.webproj.entity.Member;
import colony.webproj.service.EmailService;
import colony.webproj.sse.dto.NotificationCountDto;
import colony.webproj.sse.dto.NotificationDto;
import colony.webproj.sse.dto.NotificationHistoryDto;
import colony.webproj.sse.model.Notification;
import colony.webproj.sse.model.NotificationContent;
import colony.webproj.sse.model.NotificationType;
import colony.webproj.sse.model.RelatedURL;
import colony.webproj.sse.repository.EmitterRepository;
import colony.webproj.sse.repository.EmitterRepositoryImpl;
import colony.webproj.sse.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long userId, String lastEventId) {
        //emitter 하나하나 에 고유의 값을 주기 위해
        String emitterId = makeTimeIncludeId(userId);

        Long timeout = 60L * 1000L; //1분
        // 생성된 emiiterId를 기반으로 emitter를 저장
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));

        //emitter의 시간이 만료된 후 레포에서 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위해 처음 연결 진행 시 더미 데이터를 전달
        String eventId = makeTimeIncludeId(userId);
        // 수 많은 이벤트 들을 구분하기 위해 이벤트 ID에 시간을 통해 구분을 해줌
        sendNotification(emitter, eventId, emitterId, "연결완료");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }


    // SseEmitter를 구분 -> 구분자로 시간을 사용함 ,
    // 시간을 붙혀주는 이유 -> 브라우저에서 여러개의 구독을 진행 시
    //탭 마다 SssEmitter 구분을 위해 시간을 붙여 구분하기 위해 아래와 같이 진행
    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    // 유효시간이 다 지난다면 503 에러가 발생하기 때문에 더미데이터를 발행
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }
    // Last - event - id 가 존재한다는 것은 받지 못한 데이터가 있다는 것이다.
    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    // 받지못한 데이터가 있다면 last - event - id를 기준으로 그 뒤의 데이터를 추출해 알림을 보내주면 된다.
    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
    // =============================================
    /*
        : 실제 다른 사용자가 알림을 보낼 수 있는 기능이 필요
        알림을 구성 후 해당 알림에 대한 이벤트를 발생
        -> 어떤 회원에게 알림을 보낼지에 대해 찾고 알림을
        받을 회원의 emitter들을 모두 찾아 해당 emitter를 Send
     */

    @Async
    public void send(Notification notification) {
        //알림 저장
        String receiverId = String.valueOf(notification.getReceiver().getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();

        //에미터가 있다 -> 현재 접속중이고 알림을 받을 수 있다.
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationDto.create(notification));
                }
        );
    }
    public Notification createNotification(Member receiver, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(new RelatedURL(url))
                .isRead(false) // 현재 읽음상태
                .build();
    }

    @Transactional
    public List<NotificationHistoryDto> findAllNotifications(Long memberId) {
        List<Notification> notifications = notificationRepository.findAllByMemberId(memberId);
        List<NotificationHistoryDto> notificationHistoryDtoList = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();

        for (Notification notification : notifications) {
            LocalDateTime createdAt = notification.getCreatedAt();

            //도착 시간 계산
            long minutesDiff = ChronoUnit.MINUTES.between(createdAt, currentTime);
            long hoursDiff = ChronoUnit.HOURS.between(createdAt, currentTime);
            long daysDiff = ChronoUnit.DAYS.between(createdAt, currentTime);
            long monthsDiff = ChronoUnit.MONTHS.between(createdAt, currentTime);

            String arriveTime = "";

            // 시간 간격에 따라 메시지를 출력합니다.
            if (monthsDiff > 0) {
                arriveTime = monthsDiff + "달 전에 도착했습니다.";
            } else if (daysDiff > 0) {
                arriveTime = daysDiff + "일 전에 도착했습니다.";
            } else if (hoursDiff > 0) {
                arriveTime = hoursDiff + "시간 전에 도착했습니다.";
            } else if (minutesDiff > 0) {
                arriveTime = minutesDiff + "분 전에 도착했습니다.";
            } else {
                arriveTime = "방금 도착했습니다.";
            }
            NotificationHistoryDto notificationHistoryDto = new NotificationHistoryDto(notification, arriveTime);
            notificationHistoryDtoList.add(notificationHistoryDto);
        }
        return notificationHistoryDtoList;
    }


    @Transactional
    public void readNotification(Long notificationId) {
        //알림을 받은 사람의 id 와 알림의 id 를 받아와서 해당 알림을 찾는다.
        Notification checkNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림 id에 해당하는 엔티티가 없습니다."));
        checkNotification.read(); // 읽음처리
    }

    @Transactional(readOnly = true)
    public NotificationCountDto countUnReadNotifications(Long memberId) {
        //유저의 알람리스트에서 ->isRead(false)인 갯수를 측정 ,
        Long count = notificationRepository.countUnReadNotifications(memberId);
        return NotificationCountDto.builder()
                .count(count)
                .build();
    }

    @Transactional
    public void deleteNotificationById(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Transactional
    public void deleteNotificationAll(Long memberId) {
        notificationRepository.deleteAllByMemberId(memberId);
    }

    @Transactional
    public void readNotificationAll(Long memberId) {
        notificationRepository.updateReadAllNotification(memberId);
    }

    //현재 로그인한 회원과 알람의 주인이 일치하는지 검사
    @Transactional
    public Boolean validateAlarmAndMember(Long memberId, Long notificationId) {
        Optional<Notification> notification = notificationRepository.findByNotificationIdAndMemberId(memberId, notificationId);
        return notification.isPresent() ? true : false;
    }
}
