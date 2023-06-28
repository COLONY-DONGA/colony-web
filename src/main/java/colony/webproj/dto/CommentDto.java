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
     * 부모 댓글을 위한 from 이라서 parentId는 필요없지 싶음
     */
    public static CommentDto from(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                null,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                null,
                makeChildListDto(comment.getChildList())
                );
    }

    /**
     *  이 생성자는 자식 댓글을 위한 생성자임
     */
    public CommentDto(Comment child) {
        this.commentId = child.getId();
        this.content = child.getContent();
        this.createdAt = child.getCreatedAt();
        this.updatedAt = child.getUpdatedAt();
        this.parentId = child.getParent().getId();
    }

    public static List<CommentDto> makeChildListDto(List<Comment> child) {
        List<CommentDto> childList = new ArrayList<>();
        for (Comment comment : child) {
            childList.add(new CommentDto(comment));
        }
        return childList;
    }
}

