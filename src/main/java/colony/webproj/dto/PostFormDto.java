package colony.webproj.dto;

import colony.webproj.entity.Image;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostFormDto {
    private Long postId; //update 에서 사용
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    private String content;
    private List<MultipartFile> imageList;
    private List<ImageDto> imageDtoList;
}
