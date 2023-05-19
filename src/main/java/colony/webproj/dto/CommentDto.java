package colony.webproj.dto;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Comment parent;
    private List<CommentDto> childList;

    @QueryProjection
    public CommentDto(Long commentId, String content, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, Comment parent) {
        this.commentId = commentId;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parent = parent;
    }
}

