package colony.webproj.repository.CommentRepository;

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
    @Override
    public List<CommentDto> findParentCommentByAnswerId(Long answerId) {
        List<CommentDto> parentComment = queryFactory
                .select(new QCommentDto(
                        comment.id,
                        comment.content,
                        member.nickname,
                        comment.createdAt,
                        comment.updatedAt
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.answer.id.eq(answerId))
                .where(comment.parent.id.isNull())
                .fetch();
        return parentComment;
    }

    @Override
    public List<CommentDto> findChildCommentByAnswerId(Long answerId) {
        List<CommentDto> childComment = queryFactory
                .select(new QCommentDto(
                        comment.id,
                        comment.content,
                        member.nickname,
                        comment.createdAt,
                        comment.updatedAt,
                        comment.parent.id
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.answer.id.eq(answerId))
                .where(comment.parent.id.isNotNull())
                .fetch();
        return childComment;
    }
}
