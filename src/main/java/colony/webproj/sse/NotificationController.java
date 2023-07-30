package colony.webproj.sse;

import colony.webproj.security.PrincipalDetails;
import colony.webproj.sse.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class NotificationController {


    private final NotificationService notificationService;

    // MIME TYPE - text/event-stream 형태로 받아야함.
    // 클라이어트로부터 오는 알림 구독 요청을 받는다.
    // 로그인한 유저는 SSE 연결
    // lAST_EVENT_ID = 이전에 받지 못한 이벤트가 존재하는 경우 [ SSE 시간 만료 혹은 종료 ]
    // 전달받은 마지막 ID 값을 넘겨 그 이후의 데이터[ 받지 못한 데이터 ]부터 받을 수 있게 한다
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @ResponseBody
    public SseEmitter subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                String lastEventId) {
        log.info("구독 컨트롤러 진입");
        return notificationService.subscribe(principalDetails.getId(), lastEventId);
    }

    //알림조회
    @GetMapping("/notifications")
    @ResponseBody
    public List<NotificationDto> findAllNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<NotificationDto> notificationDtoList = notificationService.findAllNotifications(principalDetails.getId());
        model.addAttribute("notificationDtoList", notificationDtoList);
        return notificationDtoList;
    }

    //전체목록 알림 조회에서 해당 목록 클릭 시 읽음처리 ,
    @PostMapping("/notification/read/{notificationId}")
    public void readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
    }


    //단일 알림 삭제
//    @DeleteMapping(value = "/notifications/delete/{notificationId}")
//    public ResponseEntity<Object> deleteNotification(@PathVariable Long notificationId){
//
//        notificationService.deleteByNotifications(notificationId);
//        return new ResponseEntity<>(new StatusResponseDto("알림 목록 삭제 성공",""), HttpStatus.OK);
//    }
}