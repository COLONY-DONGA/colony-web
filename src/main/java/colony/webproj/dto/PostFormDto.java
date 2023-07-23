package colony.webproj.dto;

import colony.webproj.entity.Image;
import colony.webproj.entity.Post;
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
public class PostFormDto {
    private Long postId; //update 에서 사용
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    private String content;
    private List<MultipartFile> imageList = new ArrayList<>();


    private List<ImageDto> imageDtoList = new ArrayList<>(); // 생성할 때는 안 쓰이는 거겠지?

    public PostFormDto(String title, String content, List<MultipartFile> imageList ){
        this.title = title;
        this.content = content;
        this.imageList = imageList;
    }
}
