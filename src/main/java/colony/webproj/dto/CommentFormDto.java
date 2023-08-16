package colony.webproj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentFormDto {
    private Long commentId; // update 에서 사용
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
