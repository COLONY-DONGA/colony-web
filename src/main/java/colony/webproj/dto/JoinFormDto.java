package colony.webproj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinFormDto {
    private String loginId;
    private String password;
    private String name; //이름
    private String nickname; //닉네임
    private String phoneNumber; //전화번호
    private String department; //학과
}
