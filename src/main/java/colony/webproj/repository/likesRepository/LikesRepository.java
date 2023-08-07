package colony.webproj.repository.likesRepository;

import colony.webproj.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Query("select l from Likes l where l.member.loginId=:loginId and l.answer.id=:answerId")
    Optional<Likes> findByAnswerIdAndMemberId(@Param("answerId")  Long answerId, @Param("loginId") String loginId);


//    @Query("select count()")
//    Long findHearNumByAnswerId(@Param("answerId") Long answerId);
}
