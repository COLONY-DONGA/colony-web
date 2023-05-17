package colony.webproj.service;


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
    public Long saveComment(Long boardId, CommentFromDto commentFormDto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
        Post post = postRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다"));
        Comment comment = Comment.builder()
                .content(commentFormDto.getContent())
                .member(member)
                .post(post)
                .build();
        return commentRepository.save(comment).getId();
    }
}
