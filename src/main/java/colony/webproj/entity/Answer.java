package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "answer_id")
    private Long id;
    private String content;
    @Builder.Default
    private int likes = 0; //받은 좋아요 수
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Image> imageList;


    // 좋아요 개수를 증가시키는 메소드
    public void increaseLikeCount() {
        this.likes++;
    }

    // 좋아요 개수를 감소시키는 메소드
    public void decreaseLikeCount() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

}
