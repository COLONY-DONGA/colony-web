package colony.webproj.repository.PostRepository;

import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostManageDto;
import colony.webproj.dto.QPostDto;
import colony.webproj.dto.QPostManageDto;
import colony.webproj.entity.type.SearchType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static colony.webproj.entity.QMember.*;
import static colony.webproj.entity.QPost.*;

public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostDto> findPostDtoList(SearchType searchType, String searchValue, Boolean answered, String sortBy, Pageable pageable) {
        List<PostDto> result = queryFactory
                .select(new QPostDto(
                        post.id,
                        post.title,
                        member.nickname,
                        post.createdAt,
                        post.answered
                ))
                .from(post)
                .join(post.member, member)
                .where(
                        searchValue(searchType, searchValue),
                        answeredEq(answered)
                )
                .orderBy(postOrderBy(sortBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        searchValue(searchType, searchValue),
                        answeredEq(answered)
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<PostManageDto> findPostDtoListAdmin(SearchType searchType, String searchValue, Pageable pageable) {
        List<PostManageDto> result = queryFactory
                .select(new QPostManageDto(
                        post.id,
                        post.title,
                        member.name,
                        member.nickname,
                        member.department,
                        post.createdAt,
                        post.answered
                        ))
                .from(post)
                .join(post.member, member)
                .where(searchValue(searchType, searchValue))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(searchValue(searchType, searchValue))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private OrderSpecifier<?> postOrderBy(String sortBy) {
        if (sortBy.equals("createdAt")) return post.createdAt.desc();
        if (sortBy.equals("title")) return post.title.asc();
        return null;
    }

    private BooleanExpression searchValue(SearchType searchType, String searchValue) {
        if (searchValue == null || searchType == null) return null;
        if (searchType == SearchType.TITLE) {
            return post.title.containsIgnoreCase(searchValue);
        }
        if (searchType == SearchType.CONTENT) {
            return post.content.containsIgnoreCase(searchValue);
        }
        if (searchType == SearchType.NICKNAME) {
            return member.nickname.containsIgnoreCase(searchValue);
        }
        return null;
    }

    private BooleanExpression answeredEq(Boolean answered) {
        if (answered == null) return null;
        return answered.booleanValue() == true ? post.answered.eq(true) : post.answered.eq(false);
    }
}
