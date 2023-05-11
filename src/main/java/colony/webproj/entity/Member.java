package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor // 생성자?
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member {
    @Id
    @GeneratedValue

    @Column(name = "member_id")
    private Long id;

    private String loginId;
    private String password;
    private String name; //이름
    private String nickname; //닉네임
    private String phoneNumber; //전화번호
    private String department; //학과

    @Enumerated(value = EnumType.STRING)
    private Role role; //권한

}
