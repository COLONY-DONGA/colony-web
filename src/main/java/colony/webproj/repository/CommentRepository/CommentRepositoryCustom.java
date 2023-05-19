package colony.webproj.repository.CommentRepository;

import colony.webproj.dto.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findParentCommentByPostId(Long postId);
    List<CommentDto> findChildCommentByPostId(Long postId);
}
