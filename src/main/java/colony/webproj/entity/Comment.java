package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String content; // 내용
    private String createdBy; // 작성자
    @Builder.Default
    private boolean isRemoved = false; //삭제 됐는지 체크 //댓글에 대댓글이 달린 경우 댓글을 삭제해도 대댓글은 삭제 X

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //다른 방법 써볼게
//    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Reply> replyList;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Comment> childList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; //부모 댓글 id 참조
}




