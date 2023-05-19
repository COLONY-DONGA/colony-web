package colony.webproj.repository.CommentRepository;

import colony.webproj.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    List<Comment> findByPostId(@Param("postId") Long postId);

}
