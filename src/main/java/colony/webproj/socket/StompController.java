package colony.webproj.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessageSendingOperations messagingTemplate;

    //@DestinationVariable은 @PathVariable이랑 비슷

    //pub/message 로 발행을 요청하면 여기서 처리
    //  /app/alarm 이 실제 경로가 됨
    @MessageMapping("/alarm")
    public void ttt(@Payload AlarmDto alarmDto) {
        if(alarmDto.getCode().equals("답변")) {
            alarmDto.setMessage(alarmDto.getSendId() + "님이 답변을 등록했습니다.");
        }
        messagingTemplate.convertAndSend("/queue/alarm/" + alarmDto.getReceiveId(), alarmDto);
        //수신자는 footer 에서 자기 자신을 구독하고 있으면 됨
    }

}
