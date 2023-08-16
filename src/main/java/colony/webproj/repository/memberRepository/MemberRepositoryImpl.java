package colony.webproj.repository.memberRepository;


import colony.webproj.dto.*;
import colony.webproj.entity.*;
import colony.webproj.entity.type.SearchType;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static colony.webproj.entity.QAnswer.*;
import static colony.webproj.entity.QComment.*;
import static colony.webproj.entity.QMember.*;
import static colony.webproj.entity.QPost.*;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberManageDto> findAllMemberInfo(Pageable pageable, SearchType searchType, String searchValue) {
        List<MemberManageDto> result = queryFactory
                .select(new QMemberManageDto(
                        member.id,
                        member.loginId,
                        member.name,
                        member.nickname,
                        member.phoneNumber,
                        member.department,
                        member.createdAt,
                        post.id.countDistinct(),
                        comment.id.countDistinct(),
                        answer.id.countDistinct(),
                        member.role
                ))
                .from(member)
                .leftJoin(member.posts, post)
                .leftJoin(member.comments, comment)
                .leftJoin(member.answers, answer)
                .where(search(searchType, searchValue))
                .groupBy(
                        member.id,
                        member.loginId,
                        member.name,
                        member.nickname,
                        member.phoneNumber,
                        member.department,
                        member.createdAt,
                        member.role
                )
                .orderBy(member.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(member.count())
                .from(member)
                .where(search(searchType, searchValue))
                .fetchOne();
        return new PageImpl<>(result, pageable, count);
    }

    private BooleanExpression search(SearchType searchType, String searchValue) {
        if (searchValue == null || searchType == null) return null;
        if (searchType == SearchType.NAME) {
            return member.name.containsIgnoreCase(searchValue);
        }
        if (searchType == SearchType.NICKNAME) {
            return member.nickname.containsIgnoreCase(searchValue);
        }
        if (searchType == SearchType.LOGIN_ID) {
            return member.loginId.containsIgnoreCase(searchValue);
        }
        return null;
    }


    /**
     * 멤버정보를 가져오고, 작성한 답변에 대해 받은 총 좋아요 개수도 가져옴
     */
    @Override
    public MyPageDto findMemberWithLikeCount(String loginId) {

        Member memberEntity = queryFactory.selectFrom(member)
                .leftJoin(member.posts, post)
                .leftJoin(member.comments, comment)
                .leftJoin(member.answers, answer)
                .where(member.loginId.eq(loginId),
                        comment.isRemoved.eq(Boolean.FALSE)
                )
                .fetchOne();



        MyPageDto myPageDto = new MyPageDto(memberEntity);

        return myPageDto;
    }

    @Override
    public MemberPageDto findMemberInfo(String loginId) {
        QLikes likes = new QLikes("likes");
        List<MemberPageDto.PostDto> postDtoList = queryFactory
                .select(new QMemberPageDto_PostDto(post.id, post.title))
                .from(post)
                .where(post.member.loginId.eq(loginId))
                .fetch();

        List<MemberPageDto.AnswerDto> answerDtoList = queryFactory
                .select(new QMemberPageDto_AnswerDto(answer.id, post.id, answer.content))
                .from(answer)
                .join(answer.post, post)
                .where(answer.member.loginId.eq(loginId))
                .fetch();

        List<MemberPageDto.CommentDto> commentDtoList = queryFactory
                .select(new QMemberPageDto_CommentDto(comment.id, post.id, comment.content, comment.isRemoved))
                .from(comment)
                .join(answer.post, post)
                .where(comment.member.loginId.eq(loginId))
                .fetch();

        MemberPageDto memberPageDto = queryFactory
                .select(new QMemberPageDto(
                        member.loginId,
                        member.password,
                        member.name,
                        member.nickname,
                        member.email,
                        member.emailAlarm,
                        member.phoneNumber,
                        member.department,
                        JPAExpressions.select(likes.count())
                                .from(likes)
                                .where(likes.member.loginId.eq(loginId))
                        ))
                .from(member)
                .where(member.loginId.eq(loginId))
                .fetchOne();
        memberPageDto.setPostDtoList(postDtoList);
        memberPageDto.setAnswerDtoList(answerDtoList);
        memberPageDto.setCommentDtoList(commentDtoList);
        return memberPageDto;
    }
}
