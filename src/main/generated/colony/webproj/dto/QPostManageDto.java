package colony.webproj.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * colony.webproj.dto.QPostManageDto is a Querydsl Projection type for PostManageDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostManageDto extends ConstructorExpression<PostManageDto> {

    private static final long serialVersionUID = -1920919654L;

    public QPostManageDto(com.querydsl.core.types.Expression<Long> postId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> department, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<Boolean> answered) {
        super(PostManageDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, java.time.LocalDateTime.class, boolean.class}, postId, title, name, nickname, department, createdAt, answered);
    }

}

