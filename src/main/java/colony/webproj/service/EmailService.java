package colony.webproj.service;

import colony.webproj.entity.Member;
import colony.webproj.sse.model.Notification;
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

    public Boolean sendMail(Member receiver, Notification notification) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(receiver.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("콜로니 웹 사이트에 새로운 알림이 있습니다."); // 메일 제목

            //todo: 배포 시 경로 변경
            mimeMessageHelper.setText(notification.getContent() + "<br>" + "localhost:8080/" + notification.getUrl(), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
            log.info("Success");
            return true;
        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
    }
}
