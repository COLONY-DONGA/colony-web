package colony.webproj.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordFormDto {
    @NotBlank
    private String existing_password;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String newPasswordConfirm;
}
