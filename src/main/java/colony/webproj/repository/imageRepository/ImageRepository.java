package colony.webproj.repository.imageRepository;

import colony.webproj.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);
    List<Image> findByAnswerId(Long answerId);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.answer IN (SELECT a FROM Answer a WHERE a.post.id = :postId)")
    int deleteImagesByAnswerInPost(@Param("postId") Long postId);

    @Modifying
    @Query("DELETE FROM Image i where i.answer.id = :answerId")
    int deleteImagesByAnswerId(@Param("answerId") Long answerId);
}
