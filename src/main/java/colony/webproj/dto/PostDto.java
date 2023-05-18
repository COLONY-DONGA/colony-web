package colony.webproj.dto;

import colony.webproj.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt; //생성일
    private LocalDateTime updatedAt; //수정일
    private Boolean Answered; //답변 유무
    //사진
    //댓글 관련

    /**
     * postList 에 사용
     */
    @QueryProjection
    public PostDto(Long id, String title, String createdBy, LocalDateTime createdAt, Boolean answered) {
        this.postId = id;
        this.title = title;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        Answered = answered;
    }

    /**
     * Post객체를 PostDto로 변환하기 위한 함수
     */
    public static PostDto from(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isAnswered()
        );
    }
}
