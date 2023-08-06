package colony.webproj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerFormDto {
    private Long answerId; //update 에서 사용
    @NotBlank(message = "답변을 입력해주세요")
    private String content;
    private List<MultipartFile> imageList;
    private List<ImageDto> imageDtoList;

    public AnswerFormDto(String content, List<MultipartFile> imageList){
        this.content = content;
        this.imageList = imageList;
    }
}
