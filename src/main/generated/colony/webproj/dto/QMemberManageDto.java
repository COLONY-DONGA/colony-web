package colony.webproj.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * colony.webproj.dto.QMemberManageDto is a Querydsl Projection type for MemberManageDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberManageDto extends ConstructorExpression<MemberManageDto> {

    private static final long serialVersionUID = -1364543776L;

    public QMemberManageDto(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> loginId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> phoneNumber, com.querydsl.core.types.Expression<String> department, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<Long> postNum, com.querydsl.core.types.Expression<Long> commentNum, com.querydsl.core.types.Expression<Long> answerNum, com.querydsl.core.types.Expression<colony.webproj.entity.Role> role) {
        super(MemberManageDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, String.class, java.time.LocalDateTime.class, long.class, long.class, long.class, colony.webproj.entity.Role.class}, memberId, loginId, name, nickname, phoneNumber, department, createdAt, postNum, commentNum, answerNum, role);
    }

}

