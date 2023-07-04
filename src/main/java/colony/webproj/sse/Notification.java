package colony.webproj.sse;

import colony.webproj.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String content; //알림 내용

    private String url; //관련 링크

    @Column(nullable = false)
    private Boolean isRead; //읽었는지에 대한 여부

    @Column(nullable = false)
    private String notificationType; //알림에 대한 종류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member receiver;
}
