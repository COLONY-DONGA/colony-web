package colony.webproj.repository;

import colony.webproj.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("select a from Answer a left join fetch a.imageList i join a.member m where a.id= :answerId")
    Optional<Answer> findAnswerDetail(@Param("answerId") Long answerId);
}
