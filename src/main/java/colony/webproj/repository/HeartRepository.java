package colony.webproj.repository;

import colony.webproj.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByAnswerIdAndMemberId(Long answerId, String loginId);

//    @Query("select count()")
//    Long findHearNumByAnswerId(@Param("answerId") Long answerId);
}
