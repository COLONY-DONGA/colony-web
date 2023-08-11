package colony.webproj.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateFormDto {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    private String content;
    private List<MultipartFile> imageList;
    private List<String> deleteImageList;

}
