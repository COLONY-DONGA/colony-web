package colony.webproj.service;

import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostFormDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Post;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /**
     * 게시글 저장
     */
    public Long savePost(PostFormDto postFormDto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Post postEntity = Post.builder()
                .title(postFormDto.getTitle())
                .content(postFormDto.getContent())
                .createdBy(member.getNickname())
                .member(member)
                .build();
        /**
         * 파일 추가해야함
         */
        return postRepository.save(postEntity).getId();
    }

    /**
     * 게시글 작성자 조회
     */
    public String findWriter(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        return post.getMember().getLoginId();
    }
    

    /**
     * 게시글 수정, 파일 추가해야함
     */
//    public void updatePost(Long postId, PostFormDto postFormDto) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(()-> new EntityNotFoundException("게시글이 존재하지 않습니다."));
//        post.setTitle(postFormDto.getTitle());
//        post.setContent(postFormDto.getContent());
//        post.setFileList(postFormDto.get);
//    }


}
