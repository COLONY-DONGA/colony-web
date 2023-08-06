package colony.webproj.dto;

import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberFormDto {
    private String loginId;
    private String password;
    @NotBlank
    private String name; //이름
    private String nickname; //닉네임
    @NotBlank
    private String email; //이메일
    @NotBlank
    private String phoneNumber; //전화번호
    @NotBlank
    private String department; //학과
}
