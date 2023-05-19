package colony.webproj.repository;

import colony.webproj.dto.CommentDto;
import colony.webproj.dto.QCommentDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static colony.webproj.entity.QComment.*;
import static colony.webproj.entity.QMember.*;

public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
//    public List<CommentDto> findByPostId(Long postId) {
//        List<CommentDto> result = queryFactory
//                .select(new QCommentDto(
//                        comment.id,
//                        comment.content,
//                        member.nickname,
//                        comment.createdAt,
//                        comment.updatedAt,
//                        comment.parent
//                ))
//                .from(comment)
//                .innerJoin(comment.member, member)
//                .fetchJoin()
//                .innerJoin(comment.parent, comment)
//                .fetchJoin()
//                .where(comment.post.id.eq(postId))
//                .fetch();
//        return result;
//    }
}
