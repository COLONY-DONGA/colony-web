package colony.webproj.service;


import colony.webproj.dto.CommentDto;
import colony.webproj.dto.CommentFromDto;
import colony.webproj.entity.Comment;
import colony.webproj.entity.Member;
import colony.webproj.entity.Post;
import colony.webproj.repository.CommentRepository;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 생성
     */
    public Long saveComment(Long postId, CommentFromDto commentFormDto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다"));
        Comment comment = Comment.builder()
                .content(commentFormDto.getContent())
                .member(member)
                .post(post)
                .build();
        return commentRepository.save(comment).getId();
    }

    /**
     * 대댓글 생성
     */
    public Long saveReComment(Long postId, Long commentId, CommentFromDto commentFromDto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다"));
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다"));
        Comment childComment = Comment.builder()
                .content(commentFromDto.getContent())
                .member(member)
                .post(post)
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
    public Long updateCommentOrRecomment(Long commentId, CommentFromDto commentFromDto, String loginId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다"));
        comment.setContent(commentFromDto.getContent());
        return comment.getId();
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(Long commentId, CommentFromDto commentFromDto, String loginId) {
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
     * 댓글 불러오기
     */
//    public List<CommentDto> hierarchicalComment(Long postId) {
//        log.info("repo 실행");
//        List<Comment> commentList = commentRepository.findByPostId(postId);
//        log.info("repo 종료");
//
//        List<CommentDto> commentDtoList = commentList.stream().map(comment -> new CommentDto(comment)).collect(Collectors.toList());
//
//        List<CommentDto> parentComment = new ArrayList<>();
//        Map<Long, CommentDto> commentMap = new HashMap<>();
//
//        log.info("1");
//        commentDtoList.stream().forEach(commentDto -> {
//            commentMap.put(commentDto.getCommentId(), commentDto);
//        });
//        log.info("2");
//        commentDtoList.stream().forEach(commentDto -> {
//            if (commentDto.getParent() != null) {
//                commentMap.get(commentDto.getParent().getId()).getChildList().add(commentDto);
//            }
//        });
//        log.info("3");
//        commentDtoList.stream().forEach(commentDto -> {
//            if (commentDto.getParent() == null) {
//                CommentDto parent = commentMap.get(commentDto.getCommentId());
//                parentComment.add(parent);
//            }
//        });
//        log.info("4");
//        return parentComment;
//    }
    /* 댓글 계층 정렬 */
    public List<CommentDto> convertNestedStructure(Long postId) {
        List<CommentDto> comments = commentRepository.findByPostId(postId);
        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();

        comments.stream().forEach(comment -> {
            map.put(comment.getCommentId(), comment);

            /* 부모 댓글 존재 */
            if (comment.getParent() != null) {
                map.get(comment.getParent().getId()).getChildList().add(comment);
            } else result.add(comment);
        });
        return result;
    }
}
