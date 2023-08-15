package colony.webproj.dto;

import colony.webproj.entity.Answer;
import colony.webproj.entity.Comment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    private String content;
    private String createdBy;
    private Boolean isRemoved; //삭제 됐는지
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentId;
    private List<CommentDto> childList = new ArrayList<>();

    @QueryProjection
    public CommentDto(Long commentId, String content, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.commentId = commentId;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @QueryProjection
    public CommentDto(Long commentId, String content, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, Long parentId) {
        this.commentId = commentId;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentId = parentId;
    }

    /**
     *  부모 Comment Dto 변환
     */
    public CommentDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.createdBy = comment.getMember().getNickname();
        this.isRemoved = comment.isRemoved();

        //자식 comment Dto 변환
        this.childList = comment.getChildList().stream()
                .map(parent -> CommentDto.builder()
                        .commentId(parent.getId())
                        .content(parent.getContent())
                        .parentId(parent.getParent().getId())
                        .createdAt(parent.getCreatedAt())
                        .updatedAt(parent.getUpdatedAt())
                        .createdBy(parent.getMember().getNickname())
                        .build())
                .collect(Collectors.toList());
    }
}

