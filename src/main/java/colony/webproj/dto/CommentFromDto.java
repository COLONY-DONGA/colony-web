package colony.webproj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentFromDto {
    private Long commentId; // update 에서 사용
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
