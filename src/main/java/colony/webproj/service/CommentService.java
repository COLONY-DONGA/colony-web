package colony.webproj.service;


import colony.webproj.dto.CommentDto;
import colony.webproj.dto.CommentFormDto;
import colony.webproj.entity.Answer;
import colony.webproj.entity.Comment;
import colony.webproj.entity.Member;
import colony.webproj.repository.answerRepository.AnswerRepository;
import colony.webproj.repository.CommentRepository.CommentRepository;
import colony.webproj.repository.memberRepository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;

    /**
     * 댓글 생성
     */
    public Long saveComment(Long answerId, CommentFormDto commentFormDto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다"));
        Comment comment = Comment.builder()
                .content(commentFormDto.getContent())
                .member(member)
                .answer(answer)
                .build();
        return commentRepository.save(comment).getId();
    }

    /**
     * 대댓글 생성
     */
    public Long saveReComment(Long answerId, Long commentId, CommentFormDto commentFormDto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다"));
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다"));
        Comment childComment = Comment.builder()
                .content(commentFormDto.getContent())
                .member(member)
                .answer(answer)
                .parent(parentComment)
                .build();
        return commentRepository.save(childComment).getId();
    }

    /**
     * 댓글 작성자 찾기
     */
    @Transactional(readOnly = true)
    public String findWriter(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다"));
        return comment.getMember().getLoginId();
    }

    /**
     * 댓글 수정
     * 대댓글 수정
     */
    public Long updateCommentOrRecomment(Long commentId, CommentFormDto commentFormDto, String loginId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다"));
        comment.setContent(commentFormDto.getContent());
        return comment.getId();
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다"));

        //자식이 있을 땐 삭제 체크만 하고 db 에선 지우지 않음
        if (!comment.getChildList().isEmpty()) {
            comment.setRemoved(true);
        } else {
            //자식이 없다면 바로 삭제
            commentRepository.deleteById(comment.getId());
            //현재 지우려고 하는 댓글이 아래 조건을 만족하면 부모 댓글도 삭제
            //1. 대댓글이고
            //2. 부모의 대댓글이 자신 뿐이고
            //3. 부모의 댓글이 이미 지워져있다면
            if (comment.getParent() != null &&
                    comment.getParent().getChildList().size() == 1 &&
                    comment.getParent().isRemoved()) {
                commentRepository.deleteById(comment.getParent().getId());
            }
        }
    }

    /**
     * 게시글에 포함된 댓글 전부 삭제
     */
    public void deleteCommentInAnswer(Long answerId) {
        commentRepository.deleteChildByAnswerId(answerId); //자식부터 제거 후
        commentRepository.deleteParentByAnswerId(answerId); //부모 제거
    }

    /**
     * 댓글 불러오기
     */
    @Transactional(readOnly = true)
    public List<CommentDto> convertNestedStructure(Long answerId) {
        List<CommentDto> parentCommentDto = commentRepository.findParentCommentByAnswerId(answerId);
        List<CommentDto> childCommentDto = commentRepository.findChildCommentByAnswerId(answerId);

        Map<Long, CommentDto> commentDtoMap = new HashMap<>();
        List<CommentDto> responseCommentDto = new ArrayList<>();

        for(CommentDto commentDto : parentCommentDto) {
            commentDtoMap.put(commentDto.getCommentId(), commentDto);
        }
        for(CommentDto commentDto : childCommentDto) {
            commentDtoMap.get(commentDto.getParentId()).getChildList().add(commentDto);
        }

        for(CommentDto commentDto : commentDtoMap.values()) {
            responseCommentDto.add(commentDto);
        }
        return responseCommentDto;
    }
}
