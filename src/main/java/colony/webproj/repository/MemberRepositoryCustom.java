package colony.webproj.repository;

import colony.webproj.dto.MemberMangeDto;
import colony.webproj.dto.QMemberMangeDto;
import colony.webproj.entity.QComment;
import colony.webproj.entity.QMember;
import colony.webproj.entity.QPost;
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

import static colony.webproj.entity.QComment.*;
import static colony.webproj.entity.QMember.*;
import static colony.webproj.entity.QPost.*;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustom {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public Page<MemberMangeDto> findAllMemberInfo(Pageable pageable, SearchType searchType, String searchValue) {
        List<MemberMangeDto> result = queryFactory
                .select(new QMemberMangeDto(
                        member.id,
                        member.loginId,
                        member.name,
                        member.nickname,
                        member.phoneNumber,
                        member.department,
                        member.createdAt,
                        post.id.countDistinct(),
                        comment.id.countDistinct(),
                        member.role
                ))
                .from(member)
                .leftJoin(member.postList, post)
                .leftJoin(member.commentList, comment)
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
        if(searchValue == null || searchType == null) return null;
        if(searchType == SearchType.NAME) {
            return member.name.containsIgnoreCase(searchValue);
        }
        if(searchType == SearchType.NICKNAME) {
            return member.nickname.containsIgnoreCase(searchValue);
        }
        if(searchType == SearchType.LOGIN_ID) {
            return member.loginId.containsIgnoreCase(searchValue);
        }
        return null;
    }
}
