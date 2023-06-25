package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor // 생성자?
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String loginId; // 로그인 아이디
    private String password; // 비밀번호
    private String name; //이름
    private String nickname; //닉네임
    private String phoneNumber; //전화번호
    private String department; //학과

    @Enumerated(value = EnumType.STRING)
    private Role role; //권한

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Answer> answers;

    public List<Post> getMyPosts(){
        return this.getPosts();
    }

    public List<Comment> getMyComments(){
        return this.getComments();
    }

    public List<Answer> getMyAnswers(){
        return this.getAnswers();
    }




}
