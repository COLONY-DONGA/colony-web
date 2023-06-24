package colony.webproj.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * colony.webproj.dto.QPostDto is a Querydsl Projection type for PostDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostDto extends ConstructorExpression<PostDto> {

    private static final long serialVersionUID = -817304769L;

    public QPostDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> createdBy, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<Boolean> answered) {
        super(PostDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDateTime.class, boolean.class}, id, title, createdBy, createdAt, answered);
    }

}

