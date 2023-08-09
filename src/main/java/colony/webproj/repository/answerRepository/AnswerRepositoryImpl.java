package colony.webproj.repository.answerRepository;

import colony.webproj.entity.Answer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static colony.webproj.entity.QAnswer.answer;
import static colony.webproj.entity.QComment.comment;
import static colony.webproj.entity.QImage.image;
import static colony.webproj.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepositoryCustom{
    private final JPAQueryFactory queryFactory;



    @Override
    public List<Answer> findByPostId(Long postId) {
        List<Answer> answers = queryFactory
                .selectFrom(answer)
                .leftJoin(answer.imageList, image).fetchJoin()
                .join(answer.member, member).fetchJoin()
                .where(answer.post.id.eq(postId))
                .fetch();

        return answers;
    }
}
