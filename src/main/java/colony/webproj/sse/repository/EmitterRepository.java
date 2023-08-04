package colony.webproj.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter); // emitter 저장
    void saveEventCache(String emitterId, Object event); // 이벤트 저장
    Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId);
    // 해당 회원과 관련된 모든 emitter 찾는다 .
    Map<String,Object> findAllEventCacheStartWithByUserId(String userId);
    // 해당 회원과 관련된 모든 이벤트를 찾는다. - 브라우저 당 여러개 연결이 가능하기에 여러 Emitter가 존재
    void deleteById(String id);
    // emitter 삭제
    void deleteAllEmitterStartWithId(String userId);
    // 해당 회원과 관련된 모든 emitter 삭제
    void deleteAllEventCacheStartWithId(String userId);
    // 해당 회원과 관련된 모든 이벤트 삭제


}