package colony.webproj.repository.CommentRepository;

import colony.webproj.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    List<Comment> findByAnswerId(@Param("answerId") Long answerId);

    @Modifying
    @Query("delete from Comment c where c.answer.id=:answerId and c.parent is not null")
    int deleteChildByAnswerId(@Param("answerId") Long answerId);

    @Modifying
    @Query("delete from Comment c where c.answer.id=:answerId and c.parent is null")
    int deleteParentByAnswerId(@Param("answerId") Long answerId);
}
