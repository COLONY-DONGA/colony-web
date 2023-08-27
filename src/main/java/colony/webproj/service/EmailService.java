package colony.webproj.service;

import colony.webproj.entity.Member;
import colony.webproj.sse.model.Notification;
import colony.webproj.sse.model.NotificationType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;


    public Boolean sendMail(Member receiver, String content, String url, NotificationType notificationType) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(receiver.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("콜로니 웹 사이트에 새로운 알림이 있습니다."); // 메일 제목

            String type = "";
            if (notificationType == NotificationType.ANSWER) {
                type = "답변";
            }
            if (notificationType == NotificationType.COMMENT) {
                type = "댓글";
            }

            mimeMessageHelper.setText(content + "<br>"
                    + "아래 링크를 클릭해 새로 작성된 " + type + "을 확인해보세요!" + "<br>"
                    + "https://dau-colony.kro.kr/" + url + "<br>"
                    + "<img src=\"https://colony-web-bucket.s3.ap-northeast-2.amazonaws.com/colony.jpg\" width=\"540\" height=\"173\">", true);
            javaMailSender.send(mimeMessage);

            log.info("Email Send Success");
            return true;
        } catch (MessagingException e) {
            log.info("Email Send fail");
            throw new RuntimeException(e);
        }
    }
}
