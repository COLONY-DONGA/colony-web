package colony.webproj.repository;

import colony.webproj.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByAnswerIdAndMemberId(Long answerId, String loginId);

    @Query("select count()")
    Long findHearNumByAnswerId(@Param("answerId") Long answerId)
}
