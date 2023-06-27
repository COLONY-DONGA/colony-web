package colony.webproj.dto;

import colony.webproj.entity.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageDto {
    private String loginId;
    private String password;
    private String name; //이름
    private String nickname; //닉네임
    private String phoneNumber; //전화번호
    private String department; //학과
    private int likesCount; // 본인의 모든 Answer에 대한 좋아요 합계

    private List<AnswerDtoForMemberPage> answerDto;
    private List<PostDtoForMemberPage> postDto;
    private List<CommentDtoForMemberPage> commentDto;


    public MyPageDto(Member entity) {
        this.loginId = entity.getLoginId();
        this.password = entity.getPassword();
        this.name = entity.getName();
        this.nickname = entity.getNickname();
        this.phoneNumber = entity.getPhoneNumber();
        this.department = entity.getDepartment();
        this.likesCount = 0;

        if (entity.getPosts().size() != 0) {
            List<PostDtoForMemberPage> postDtoList = new ArrayList<>();
            for (Post post : entity.getPosts()) {
                PostDtoForMemberPage postDto = new PostDtoForMemberPage(post.getId(), post.getTitle());
                postDtoList.add(postDto);
            }
            this.postDto = postDtoList;
        }

        if (entity.getAnswers().size() != 0) {
            List<AnswerDtoForMemberPage> answerDtoList = new ArrayList<>();
            for (Answer answer : entity.getAnswers()) {
                AnswerDtoForMemberPage answerDto = new AnswerDtoForMemberPage(answer.getId(), answer.getPost().getId(), answer.getContent());
                answerDtoList.add(answerDto);
            }
            this.answerDto = answerDtoList;
        }

        if (entity.getComments().size() != 0) {
            List<CommentDtoForMemberPage> commentDtoList = new ArrayList<>();

            for (Comment comment : entity.getComments()) {
                CommentDtoForMemberPage commentDto = new CommentDtoForMemberPage(comment.getId(), comment.getAnswer().getId(), comment.getContent());
                commentDtoList.add(commentDto);
            }
            this.commentDto = commentDtoList;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class AnswerDtoForMemberPage {
        private Long answerId;
        private Long postId;
        private String cotent;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class PostDtoForMemberPage {
        private Long postId;
        private String title;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class CommentDtoForMemberPage {
        private Long commentId;
        private Long answerId;
        private String cotent;
    }

}


