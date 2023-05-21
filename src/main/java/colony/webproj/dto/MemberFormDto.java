package colony.webproj.dto;

import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Builder
public class MemberFormDto {
    private String loginId;
    private String password;
    private String name; //이름
    private String nickname; //닉네임
    private String phoneNumber; //전화번호
    private String department; //학과
}
