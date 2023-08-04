package colony.webproj.dto;

import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 시큐리티에서 사용
 */
@Getter @Setter
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String loginId;
    private String password;
    private String name; //이름
    private String nickname; //닉네임
    private String email; //이메일
    private String phoneNumber; //전화번호
    private String department; //학과
    private Role role; //권한

    public static MemberDto from(Member entity){
        return new MemberDto(
                entity.getId(),
                entity.getLoginId(),
                entity.getPassword(),
                entity.getName(),
                entity.getNickname(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getDepartment(),
                entity.getRole()
        );
    }
}
