package colony.webproj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostFormDto {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    private String content;
    private List<MultipartFile> imageList;
}
