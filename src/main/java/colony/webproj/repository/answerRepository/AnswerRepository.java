package colony.webproj.repository.answerRepository;

import colony.webproj.entity.Answer;
import colony.webproj.entity.Post;
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

    // 승지: left join 코멘트 추가
    @Query("select a from Answer a LEFT JOIN FETCH a.comments c left join fetch a.imageList i join fetch a.member m where a.post.id=:postId")
    List<Answer> findByPostId(@Param("postId") Long postId);

    // 첫 번째 쿼리: Answer 와 comments 연관성 로딩
    @Query("select a from Answer a LEFT JOIN FETCH a.comments c where a.post.id=:postId")
    List<Answer> findByPostIdWithComments(@Param("postId") Long postId);




    // 두 번째 쿼리: Answer 와 imageList, member 연관성 로딩
    @Query("select a from Answer a LEFT JOIN FETCH a.imageList i join fetch a.member m where a.post.id=:postId")
    List<Answer> findByPostIdWithImageListAndMember(@Param("postId") Long postId);


    @Query("select distinct a from Answer a " +
            "left join fetch a.imageList i " +
            "join fetch a.member m " +
            "where a.post.id = :postId")
    List<Answer> findAnswersByPostId(@Param("postId") Long postId);








    @Modifying
    @Query("DELETE FROM Answer a WHERE a.post.id = :postId")
    void deleteAnswersByPostId(@Param("postId") Long postId);
}
