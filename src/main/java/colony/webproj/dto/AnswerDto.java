package colony.webproj.dto;

import colony.webproj.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class AnswerDto {
    private Long answerId;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt; //생성일
    private LocalDateTime updatedAt; //수정일
    private List<ImageDto> imageDtoList; // 사진
    private Long heartNum; //좋아요 수

    public AnswerDto(Answer answer) {
        this.answerId = answer.getId();
        this.content = answer.getContent();
        this.createdBy = answer.getMember().getNickname();
        this.createdAt = answer.getCreatedAt();
        this.updatedAt = answer.getUpdatedAt();
        this.imageDtoList = answer.getImageList().stream().map(image -> new ImageDto(image)).collect(Collectors.toList());
    }
}
