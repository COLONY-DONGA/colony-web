package colony.webproj.repository.PostRepository;

import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostManageDto;
import colony.webproj.dto.QPostDto;
import colony.webproj.dto.QPostManageDto;
import colony.webproj.entity.QAnswer;
import colony.webproj.entity.QMember;
import colony.webproj.entity.Role;
import colony.webproj.entity.type.SearchType;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static colony.webproj.entity.QMember.*;
import static colony.webproj.entity.QPost.*;

public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostDto> findPostDtoList(SearchType searchType, String searchValue, Boolean answered, String sortBy, Pageable pageable, Long categoryId) {
        LocalDateTime currentTime = LocalDateTime.now();
        QMember member = new QMember("member");
        QAnswer answer = new QAnswer("answer");
        List<PostDto> result = queryFactory
                .select(new QPostDto(
                        post.id,
                        post.title,
                        post.content,
                        member.nickname,
                        post.createdAt,
                        post.answered,
                        post.viewCount,
                        JPAExpressions.select(answer.count())
                                .from(answer)
                                .where(answer.post.eq(post))
                ))
                .from(post)
                .join(post.member, member)
                .where(
                        searchValue(searchType, searchValue),
                        answeredEq(answered),
                        post.isNotice.eq(false),
                        Category(categoryId)
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
                        answeredEq(answered),
                        post.isNotice.eq(false),
                        Category(categoryId)
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<PostManageDto> findPostDtoListAdmin(SearchType searchType, String searchValue, Pageable pageable) {
        QMember member1 = new QMember("member1");
        List<PostManageDto> result = queryFactory
                .select(new QPostManageDto(
                        post.id,
                        post.title,
                        member1.name,
                        member1.nickname,
                        member1.department,
                        post.createdAt,
                        post.answered
                        ))
                .from(post)
                .join(post.member, member1)
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

    @Override
    public List<PostDto> findPostDtoNotice() {
        QMember member2 = new QMember("member2");
        List<PostDto> result = queryFactory
                .select(new QPostDto(
                        post.id,
                        post.title,
                        post.content,
                        member2.nickname,
                        post.createdAt,
                        post.viewCount
                ))
                .from(post)
                .join(post.member, member2)
                .where(
                        post.isNotice.eq(true),
                        member2.role.eq(Role.ROLE_ADMIN)
                )
                .orderBy(post.createdAt.desc())
                .fetch();
        return result;
    }

    private OrderSpecifier<?> postOrderBy(String sortBy) {
        if (sortBy.equals("createdAtDesc")) return post.createdAt.desc();
        if (sortBy.equals("createdAtAsc")) return post.createdAt.asc();
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
            return post.member.nickname.containsIgnoreCase(searchValue);
        }
        return null;
    }

    private BooleanExpression Category(Long categoryId){
        if(categoryId==null){
            return null;
        }
        else{
            return post.category.id.eq(categoryId);
        }
    }

    private BooleanExpression answeredEq(Boolean answered) {
        if (answered == null) return null;
        return answered.booleanValue() == true ? post.answered.eq(true) : post.answered.eq(false);
    }
}
