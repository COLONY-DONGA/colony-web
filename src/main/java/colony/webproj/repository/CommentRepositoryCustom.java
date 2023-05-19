package colony.webproj.repository;

import colony.webproj.dto.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findByPostId(Long postId);
}
