package colony.webproj.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordFormDto {
    private String existing_password;
    private String newPassword;
    private String newPasswordConfirm;
}
