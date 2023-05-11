package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor // 생성자?
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;   // 게시글 제목
    private String content; // 본문 내용

    @ManyToOne(fetch = FetchType.LAZY) // 멤버 셀렉은 나중에.
    @JoinColumn(name = "member_id")
    private Member member;

}
