package colony.webproj.socket;

import colony.webproj.security.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    //로그인 한 전체 session 리스트
    List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
    // 현재 로그인 중인 개별 유저
    Map<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String senderId = getMemberName(session); // 접속한 유저의 http세션을 조회하여 id를 얻는 함수
        if (senderId != null) {    // 로그인 값이 있는 경우만
            log.info(senderId + " 연결 됨");
            users.put(senderId, session);   // 로그인중 개별유저 저장
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //protocol : cmd,댓글 작성자, 수신자 ID, 게시글 id, 게시글 제목
        // 클라이언트로부터의 메시지 처리
        String msg = message.getPayload();
        if (msg != null) {
            String[] strs = msg.split(",");
            log.info(strs.toString());
            if (strs != null && strs.length == 5) {
                String cmd = strs[0];
                String caller = strs[1];
                String receiver = strs[2];
                String postId = strs[3];
                String title = strs[4];
                WebSocketSession targetSession = users.get(receiver);  // 메시지를 받을 세션 조회

                // 실시간 접속시에만 알람을 보냄
                if (targetSession != null) {
                    TextMessage textMsg = new TextMessage(caller + " 님이 " + "<a href='/post/" + postId + "' style=\"color:black\"><strong>" + title + "</strong> 에 댓글을 남겼습니다.</a>");
                    targetSession.sendMessage(textMsg);
                }
            }
        }
    }

    //연결이 끊어진 후
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Socket 연결 해제");
        sessions.remove(session);
        users.remove(getMemberName(session), session);
    }

    //알람을 보내는 유저(댓글작성, 좋아요 누르는 유저)
    private String getMemberName(WebSocketSession session) {
        String loginUsername;

        if (session.getPrincipal() == null) {
            loginUsername = null;
        } else {
            loginUsername = session.getPrincipal().getName();
        }
        return loginUsername;
    }
}

