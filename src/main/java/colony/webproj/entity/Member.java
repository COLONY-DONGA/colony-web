package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String createdBy; // 작성자

    @Enumerated(value = EnumType.STRING)
    private Role role; //권한

}
