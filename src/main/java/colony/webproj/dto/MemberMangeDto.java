package colony.webproj.dto;

import colony.webproj.entity.Role;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class MemberMangeDto {
    private Long memberId;
    private String loginId;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String department;
    private LocalDateTime createdAt; //가입일자
    private Long postNum; //게시글 수
    private Long commentNum; //댓글 수
    private Role role;

    @QueryProjection
    public MemberMangeDto(Long memberId, String loginId, String name, String nickname, String phoneNumber, String department, LocalDateTime createdAt, Long postNum, Long commentNum, Role role) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.createdAt = createdAt;
        this.postNum = postNum;
        this.commentNum = commentNum;
        this.role = role;
    }
}
