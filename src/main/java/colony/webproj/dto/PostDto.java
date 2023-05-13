package colony.webproj.dto;

import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt; //생성일
    private LocalDateTime updatedAt; //수정일
    //사진
    //댓글 관련
    //생성일
    //수정일
}
