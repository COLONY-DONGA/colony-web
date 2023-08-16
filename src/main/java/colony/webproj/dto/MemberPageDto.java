package colony.webproj.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPageDto {
    private String loginId;
    private String password;
    private String name; //이름
    private String nickname; //닉네임
    private String email; //이메일
    private Boolean emailAlarm; //이메일 알람 수신 동의 여부
    private String phoneNumber; //전화번호
    private String department; //학과
    private Long likesCount; // 본인의 모든 Answer에 대한 좋아요 합계
    private List<PostDto> PostDtoList;
    private List<AnswerDto> AnswerDtoList;
    private List<CommentDto> commentDtoList;

    @QueryProjection
    public MemberPageDto(String loginId, String password, String name, String nickname, String email, Boolean emailAlarm, String phoneNumber, String department, Long likesCount) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.emailAlarm = emailAlarm;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.likesCount = likesCount;
    }

    @Getter
    public static class PostDto {
        private Long postId;
        private String title;

        @QueryProjection
        public PostDto(Long postId, String title) {
            this.postId = postId;
            this.title = title;
        }
    }
    @Getter
    public static class AnswerDto {
        private Long answerId;
        private Long postId;
        private String content;
        @QueryProjection
        public AnswerDto(Long answerId, Long postId, String content) {
            this.answerId = answerId;
            this.postId = postId;
            this.content = content;
        }
    }
    @Getter
    public static class CommentDto {
        private Long commentId;
        private Long postId;
        private String content;
        @QueryProjection
        public CommentDto(Long commentId, Long postId, String content) {
            this.commentId = commentId;
            this.postId = postId;
            this.content = content;
        }
    }
}
