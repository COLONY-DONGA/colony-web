package colony.webproj.repository;

import colony.webproj.dto.CommentDto;
import colony.webproj.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
//    @Query("select c from Comment c join fetch c.member m where c.post.id=:postId")
//    List<Comment> findByPostId(@Param("postId") Long postId);



}
