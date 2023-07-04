package colony.webproj.sse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    /**
     * emitter 저장
     */
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    /**
     * 이벤트 저장
     */
    void saveEventCache(String emitterId, Object event);

    /**
     * 회원과 관련된 모든 emitter 찾기
     */
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);

    /**
     * 회원과 관련된 모든 이벤트 찾기
     */
    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);

    /**
     * emitter 삭제
     */
    void deleteById(String id);

    void deleteAllEmitterStartWithId(String memberId);
    void deleteAllEventCacheStartWithId(String memberId);
}
