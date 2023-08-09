package colony.webproj.repository.answerRepository;

import colony.webproj.entity.Answer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepositoryCustom {

    List<Answer> findByPostId(@Param("postId") Long postId);
}
