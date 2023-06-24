package colony.webproj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AnswerFormDto {
    private Long answerId; //update 에서 사용
    @NotBlank(message = "답변을 입력해주세요")
    private String content;
    private List<MultipartFile> imageList;
    private List<ImageDto> imageDtoList;
}
