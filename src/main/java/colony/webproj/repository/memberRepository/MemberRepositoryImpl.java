package colony.webproj.repository.memberRepository;


import colony.webproj.dto.MemberManageDto;
import colony.webproj.dto.MyPageDto;
import colony.webproj.dto.QMemberManageDto;
import colony.webproj.entity.*;
import colony.webproj.entity.type.SearchType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
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
        // First, find the member
        String memberQuery = "SELECT m FROM Member m WHERE m.loginId = :loginId";
        Member member = (Member) em.createQuery(memberQuery, Member.class)
                .setParameter("loginId", loginId)
                .getSingleResult();

        // Then fetch the posts
        List<Post> posts = em.createQuery(
                        "SELECT p FROM Post p WHERE p.member = :member", Post.class)
                .setParameter("member", member)
                .getResultList();

        member.setPosts(posts);

        // Then fetch the comments
        List<Comment> comments = em.createQuery(
                        "SELECT c FROM Comment c WHERE c.member = :member", Comment.class)
                .setParameter("member", member)
                .getResultList();

        member.setComments(comments);

        // Then fetch the answers
        List<Answer> answers = em.createQuery(
                        "SELECT a FROM Answer a WHERE a.member = :member", Answer.class)
                .setParameter("member", member)
                .getResultList();

        member.setAnswers(answers);

        // Now you can create the MyPageDto with the fully fetched Member
        MyPageDto myPageDto = new MyPageDto(member);

        return myPageDto;
    }



}
