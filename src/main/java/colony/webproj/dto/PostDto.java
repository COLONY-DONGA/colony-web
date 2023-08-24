package colony.webproj.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

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
    private Integer viewCount;
    private LocalDateTime createdAt; //생성일
    private String enrollTime; // 현재로부터 등록된 날
    private LocalDateTime updatedAt; //수정일
    private Boolean Answered; //답변 유무
    private Long answerCount;
    private List<ImageDto> imageDtoList; // 사진
    private Long categoryId;
    //댓글과 답변은 따로 modelAttribute 해줌

    //승지 from 코드 생성자
    public PostDto(Long postId, String title, String content, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean answered) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.Answered = answered;

    }

    /**
     * postList 에 사용
     */
    @QueryProjection
    public PostDto(Long id, String title, String content, String createdBy, LocalDateTime createdAt, Boolean answered, Integer viewCount, Long answerCount, Long categoryId) {
        this.postId = id;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.Answered = answered;
        this.viewCount = viewCount;
        this.answerCount = answerCount;
        this.categoryId = categoryId;
    }

    /**
     * postList 공지
     */
    @QueryProjection
    public PostDto(Long id, String title, String content, String nickname, LocalDateTime createdAt, Integer viewCount) {
        this.postId = id;
        this.title = title;
        this.content = content;
        this.createdBy = nickname;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.categoryId = categoryId;
    }

}
