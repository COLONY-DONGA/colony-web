package colony.webproj.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinFormDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,12}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~12자리여야 합니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,16}$",
            message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자를 1개 이상 포함해야 합니다.")
    private String password;
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name; //이름
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname; //닉네임
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email; //닉네임
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phoneNumber; //전화번호
    @NotBlank(message = "학과는 필수 입력 값입니다.")
    private String department; //학과
}
