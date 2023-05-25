package colony.webproj.dto;

import colony.webproj.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<ImageDto> imageDtoList; // 사진


    //승지 from 코드 생성자
    public PostDto(Long postId, String title, String content, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean answered) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        Answered = answered;
    }

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
                null, //에러떠서 null 로 넣어둠.
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isAnswered()
        );
    }
}
