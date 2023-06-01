package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList;

    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false)
    private Integer likeCount;


    // 좋아요 개수를 증가시키는 메소드
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 개수를 감소시키는 메소드
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
