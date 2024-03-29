package colony.webproj.dto;

import colony.webproj.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
@Builder
@AllArgsConstructor
public class AnswerDto {
    private Long answerId;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt; //생성일
    private LocalDateTime updatedAt; //수정일
    private List<ImageDto> imageDtoList; // 사진
    private List<CommentDto> commentList;
    private Integer heartNum; //좋아요 수
    private Boolean isHearted; // 로그인한 회원이 좋아요 눌러쓴지


    // refactoring 전 코드
    public AnswerDto(Answer answer, Integer heartNum) {
        this.answerId = answer.getId();
        this.content = answer.getContent();
        this.createdBy = answer.getMember().getNickname();
        this.createdAt = answer.getCreatedAt();
        this.updatedAt = answer.getUpdatedAt();
        this.imageDtoList = answer.getImageList().stream().map(image -> new ImageDto(image)).collect(Collectors.toList());
        this.commentList = answer.getComments().stream().map(comment -> new CommentDto(comment)).collect(Collectors.toList());
        this.heartNum = heartNum;
    }

    public static AnswerDto from (Answer answer, Map<Long, Boolean> userLikedAnswers) {
        List<ImageDto> imageDtoList = answer.getImageList().stream().map(image -> new ImageDto(image)).collect(Collectors.toList());
        List<CommentDto> commentDtoList = answer.getComments().stream().map(comment -> new CommentDto(comment)).collect(Collectors.toList());
        return new AnswerDto(
                answer.getId(),
                answer.getContent(),
                answer.getMember().getNickname(),
                answer.getCreatedAt(),
                answer.getUpdatedAt(),
                imageDtoList,
                commentDtoList,
                answer.getLikes().size(),
                userLikedAnswers.getOrDefault(answer.getId(), false)
        );
    }
}
