package colony.webproj.service;

import colony.webproj.dto.ImageDto;
import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostFormDto;
import colony.webproj.entity.Image;
import colony.webproj.entity.Member;
import colony.webproj.entity.Post;
import colony.webproj.entity.type.SearchType;
import colony.webproj.repository.ImageRepository;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.PostRepository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final ImageRepository imageRepository;

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ImageService imageService;

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
            case LOGIN_ID -> null;
            case NAME -> null;
        };
    }

    /**
     * queryDsl 게시글 리스트 조회
     */
    public Page<PostDto> searchPostList(SearchType searchType, String searchValue, Boolean answered, String sortBy, Pageable pageable) {
        return postRepository.findPostDtoList(searchType, searchValue, answered, sortBy, pageable);
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
                .member(member)
                .build();
        Long savedPost = postRepository.save(postEntity).getId(); //이미지 보다 먼저 저장

        List<Image> imageList = imageService.uploadFile(postFormDto.getImageList());

        //파일이 있다면 db 저장
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                image.setPost(postEntity);
                imageRepository.save(image);
                log.info("이미지 저장 완료");
            }
        }
        return savedPost;
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
     * 게시글 업데이트
     */
    public Long updatePost(Long postId, PostFormDto postFormDto) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        post.setTitle(postFormDto.getTitle());
        post.setContent(postFormDto.getContent());

        //수정하며 추가한 사진 파일 업로드
        List<Image> imageList = imageService.uploadFile(postFormDto.getImageList());

        //파일이 있다면 db 저장
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                image.setPost(post); //연관관계 설정
                imageRepository.save(image);
                log.info("이미지 저장 완료");
            }
        }
        return post.getId();
    }

    /**
     * 게시글 조회(업데이트)
     */
    public PostFormDto updateForm(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        List<ImageDto> imageDtoList = imageRepository.findByPostId(postId).stream()
                .map(image -> new ImageDto(image))
                .collect(Collectors.toList());

        PostFormDto postFormDto = PostFormDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageDtoList(imageDtoList)
                .build();
        return postFormDto;
    }

    /**
     * 게시글 삭제
     * 댓글 등 연관된 데이터 제거 필요
     */
    public void deletePost(Long postId) {
        List<Image> imageList = imageRepository.findByPostId(postId);
        imageService.deleteFile(imageList);
        postRepository.deleteById(postId);
    }

    /**
     * 게시글 상세보기
     */
    public PostDto findPostDetail(Long postId) {
        Post post = postRepository.findPostDetail(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        List<ImageDto> imageDtoList = imageRepository.findByPostId(postId).stream()
                .map(image -> new ImageDto(image))
                .collect(Collectors.toList());
        PostDto postDto = PostDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdBy(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .Answered(post.isAnswered())
                .imageDtoList(imageDtoList) //이미지
                .build();
        return postDto;
    }
}
