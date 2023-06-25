package colony.webproj.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class PostManageDto {
    private Long postId; //postId
    private String title; //제목
    private String name; //회원 본명
    private String nickname; //회원 닉네임
    private String department; //회원 소속 학과
    private LocalDateTime createdAt; //생성일
    private Boolean Answered; //답변 유무

    /**
     * Admin 페이지 게시글 관리에서 사용
     */
    @QueryProjection
    public PostManageDto(Long postId, String title, String name, String nickname, String department, LocalDateTime createdAt, Boolean answered) {
        this.postId = postId;
        this.title = title;
        this.name = name;
        this.nickname = nickname;
        this.department = department;
        this.createdAt = createdAt;
        Answered = answered;
    }
}
