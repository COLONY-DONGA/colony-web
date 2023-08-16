package colony.webproj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentFormDto {
    private Long commentId; // update 에서 사용
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
