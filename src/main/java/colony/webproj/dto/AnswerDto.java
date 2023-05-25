package colony.webproj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AnswerDto {
    private Long answerId;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt; //생성일
    private LocalDateTime updatedAt; //수정일
    private List<ImageDto> imageDtoList; // 사진
}
