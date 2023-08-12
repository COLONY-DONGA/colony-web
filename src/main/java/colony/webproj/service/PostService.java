package colony.webproj.service;

import colony.webproj.dto.ImageDto;
import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostFormDto;
import colony.webproj.dto.PostUpdateFormDto;
import colony.webproj.entity.Image;
import colony.webproj.entity.Member;
import colony.webproj.entity.Post;
import colony.webproj.entity.type.SearchType;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.repository.imageRepository.ImageRepository;
import colony.webproj.repository.memberRepository.MemberRepository;
import colony.webproj.repository.PostRepository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final ImageRepository imageRepository;

    private final MemberRepository memberRepository;
    private final AnswerService answerService;
    private final PostRepository postRepository;
    private final ImageService imageService;


    /**
     * queryDsl 게시글 리스트 조회
     */
    public Page<PostDto> searchPostList(SearchType searchType, String searchValue, Boolean answered, String sortBy, Pageable pageable) {
        Page<PostDto> resultPage =
                postRepository.findPostDtoList(searchType, searchValue, answered, (sortBy == null) ? "createdAtDesc" : sortBy, pageable);

        List<PostDto> resultList = resultPage.getContent();
        for (PostDto postDto : resultList) {
            LocalDateTime createdAt = postDto.getCreatedAt();
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(createdAt, currentTime);

            String enrollTime = getTimeAgo(duration);
            postDto.setEnrollTime(enrollTime);
        }
        return resultPage;
    }


    /**
     * 게시글 저장
     */
    public Long savePost(PostFormDto postFormDto, String loginId) throws IOException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
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
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return post.getMember().getLoginId();
    }

    /**
     * 게시글 업데이트
     */
    public Long updatePost(Long postId, PostUpdateFormDto postUpdateFormDto) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.setTitle(postUpdateFormDto.getTitle());
        post.setContent(postUpdateFormDto.getContent());

        //수정하며 삭제할 사진 s3, db 에서 제거
        imageService.deleteFileWithStoreImageName(postUpdateFormDto.getDeleteImageList());
        imageRepository.deleteByImageIds(postUpdateFormDto.getDeleteImageList());

        //수정하며 추가한 사진 파일 업로드
        List<Image> imageList = imageService.uploadFile(postUpdateFormDto.getImageList());

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
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
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
        imageService.deleteFile(imageList); //게시글 관련 이미지 로컬에서 제거


        //답변 제거
        //답변에 대한 이미지도 로컬에서 지워야 되기 때문에 cascade 사용 x
        answerService.deleteByPostId(postId);

        postRepository.deleteById(postId); //종속된 엔티티를 전부 제거한 후 게시글 삭제
    }

    /**
     * 게시글 상세보기
     */
    public PostDto findPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.setViewCount(post.getViewCount() + 1);

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
                .viewCount(post.getViewCount())
                .imageDtoList(imageDtoList) //이미지
                .build();
        return postDto;
    }

    private String getTimeAgo(Duration duration) {
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "방금 전";
        } else if (seconds < 3600) {
            long minutes = duration.toMinutes();
            return minutes + "분 전";
        } else if (seconds < 86400) {
            long hours = duration.toHours();
            return hours + "시간 전";
        } else if (seconds < 2592000) {
            long days = duration.toDays();
            return days + "일 전";
        } else if (seconds < 31536000) {
            long months = duration.toDays() / 30;
            return months + "달 전";
        } else {
            long years = duration.toDays() / 365;
            return years + "년 전";
        }
    }

}
