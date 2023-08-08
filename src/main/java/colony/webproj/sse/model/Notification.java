package colony.webproj.sse.model;

import colony.webproj.entity.BaseEntity;
import colony.webproj.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
    //알림 내용 - 50자 이내

    @Embedded
    private RelatedURL url;
    //관련 링크 - 클릭 시 이동해야할 링크

    @Column(nullable = false)
    private Boolean isRead;
    //읽었는지에 대한 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member receiver;
    //회원정보


    public void read() {
        isRead = true;
    }


    public String getUrl() {
        return url.getUrl();
    }

}

/*
 알림 기능 처리 요소
  - 누구 : ~ 에 대한 알림이 도착했다. 형식의 알림을 클릭하면 관련 페이지로 이동하는 방식.
  - 알림을 읽으면 '읽음' 처리가 되어야한다.

 */
