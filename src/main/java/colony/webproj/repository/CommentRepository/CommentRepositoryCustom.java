package colony.webproj.repository.CommentRepository;

import colony.webproj.dto.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findParentCommentByAnswerId(Long answerId);
    List<CommentDto> findChildCommentByAnswerId(Long answerId);
}
