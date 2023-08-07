package colony.webproj.sse.dto;

import colony.webproj.sse.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NotificationHistoryDto {
    private Long id;
    private String content;
    private String url;
    private Boolean status;
    private String arriveTime; //얼마 전에 알림이 왔는지 시간, 일로 표시

    public NotificationHistoryDto(Notification notification, String arriveTime) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.url = notification.getUrl();
        this.status = notification.getIsRead();
        this.arriveTime = arriveTime;
    }
}
