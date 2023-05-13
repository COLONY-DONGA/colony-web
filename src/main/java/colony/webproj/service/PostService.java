package colony.webproj.service;

import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostFormDto;
import colony.webproj.entity.Image;
import colony.webproj.entity.Member;
import colony.webproj.entity.Post;
import colony.webproj.entity.type.SearchType;
import colony.webproj.repository.ImageRepository;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final ImageRepository imageRepository;

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ImageHandler imageHandler;

    /**
     * 게시글리스트 조회
     */
    public Page<PostDto> searchPosts(SearchType searchType, String searchKeyword, Pageable pageable) {
        // 검색어가 없으면 전체 조회
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return postRepository.findAll(pageable).map(PostDto::from);
        }
        // 검색어가 있으면 타입별로 조회
        // Repository가 반환하는 기본값은 Post이므로 이를 .map(PostDto::from) 이용해서 DTO객체로 만들어준다.
        return switch (searchType) {
            case TITLE ->
                    postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(searchKeyword, pageable).map(PostDto::from);
            case CONTENT ->
                    postRepository.findByContentContainingIgnoreCaseOrderByCreatedAtDesc(searchKeyword, pageable).map(PostDto::from);
            case NICKNAME ->
                    postRepository.findByMember_NicknameContainingOrderByCreatedAtDesc(searchKeyword, pageable).map(PostDto::from);
        };
    }

    /**
     * 게시글 저장
     */
    public Long savePost(PostFormDto postFormDto, String loginId) throws IOException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Post postEntity = Post.builder()
                .title(postFormDto.getTitle())
                .content(postFormDto.getContent())
                .createdBy(member.getNickname())
                .member(member)
                .build();
        List<Image> imageList = imageHandler.uploadFile(postFormDto.getImageList());

        //파일이 있다면 db 저장
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                image.setPost(postEntity); //연관관계 설정
                imageRepository.save(image);
            }
        }
        return postRepository.save(postEntity).getId();
    }

    /**
     * 게시글 작성자 조회
     */
    @Transactional(readOnly = true)
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
