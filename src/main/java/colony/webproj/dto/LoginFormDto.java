package colony.webproj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginFormDto {
    private String loginId;
    private String password;
}
