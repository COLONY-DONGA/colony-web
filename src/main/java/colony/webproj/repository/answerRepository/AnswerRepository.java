package colony.webproj.repository.answerRepository;

import colony.webproj.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom{

    @Query("select a from Answer a left join fetch a.imageList i join fetch a.member m where a.id= :answerId")
    Optional<Answer> findAnswerDetail(@Param("answerId") Long answerId);



    @Query("select distinct a from Answer a " +
            "left join fetch a.imageList i " +
            "join fetch a.member m " +
            "where a.post.id = :postId")
    List<Answer> findAnswersByPostId(@Param("postId") Long postId);



    @Modifying
    @Query("DELETE FROM Answer a WHERE a.post.id = :postId")
    void deleteAnswersByPostId(@Param("postId") Long postId);
}
