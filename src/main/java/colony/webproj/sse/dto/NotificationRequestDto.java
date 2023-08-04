package colony.webproj.sse.dto;

import colony.webproj.entity.Member;
import colony.webproj.sse.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private Member receiver;
    private NotificationType notificationType;
    private String content;
    private String url;

}