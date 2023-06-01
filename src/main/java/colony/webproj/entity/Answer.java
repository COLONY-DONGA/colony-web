package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Builder.Default
    private int likes = 0; // 받은 좋아요 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Heart> likesList;

    // 좋아요 개수를 계산하는 메소드
    public int getLikeCount() {
        return likesList.size();
    }

    // 좋아요 추가 메소드
    public void addLike(Heart like) {
        likesList.add(like);
        like.setAnswer(this);

    }

    // 좋아요 제거 메소드
    public void removeLike(Heart like) {
        likesList.remove(like);
        like.setAnswer(null);
    }
}
