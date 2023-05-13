package colony.webproj.dto;

import colony.webproj.entity.Post;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
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

    /**
     * Post객체를 PostDto로 변환하기 위한 함수
     */
    public static PostDto from(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
