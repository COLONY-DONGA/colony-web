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

    @OneToMany(mappedBy = "answer")
    private List<Comment> comments;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList;
}
