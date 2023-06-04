package colony.webproj.dto;

import colony.webproj.entity.Role;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class MemberManageDto {
    private Long memberId;
    private String loginId;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String department;
    private LocalDateTime createdAt; //가입일자
    private Long postNum; //게시글 수
    private Long commentNum; //댓글 수
    private Long answerNum; //답변 수
    private Role role;

    @QueryProjection
    public MemberManageDto(Long memberId, String loginId, String name, String nickname, String phoneNumber, String department, LocalDateTime createdAt, Long postNum, Long commentNum, Long answerNum, Role role) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.createdAt = createdAt;
        this.postNum = postNum;
        this.commentNum = commentNum;
        this.answerNum = answerNum;
        this.role = role;
    }
}
