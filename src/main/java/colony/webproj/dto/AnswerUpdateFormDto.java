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
public class AnswerUpdateFormDto {
    @NotBlank(message = "답변을 입력해주세요")
    private String content;
    private List<MultipartFile> imageList; // 새로 등록하는 사진
    private List<String> deleteImageList; // 기존 사진 중 제거할 사진 id
}

