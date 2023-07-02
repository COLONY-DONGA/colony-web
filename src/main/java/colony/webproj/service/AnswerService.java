package colony.webproj.service;

import colony.webproj.dto.AnswerDto;
import colony.webproj.dto.AnswerFormDto;
import colony.webproj.dto.ImageDto;
import colony.webproj.entity.*;
import colony.webproj.repository.CommentRepository.CommentRepository;
import colony.webproj.repository.answerRepository.AnswerRepository;
import colony.webproj.repository.ImageRepository;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.PostRepository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnswerService {
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    /**
     * 답변 저장
     */
    public Long saveAnswer(Long postId, String loginId, AnswerFormDto answerFormDto) throws IOException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        post.setAnswered(true); //답변 등록됨 체크
        //answer 저장
        Answer answer = Answer.builder()
                .content(answerFormDto.getContent())
                .member(member)
                .post(post)
                .build();
        Long savedAnswer = answerRepository.save(answer).getId();

        //이미지 저장 후 연관관계 설정
        List<Image> imageList = imageService.uploadFile(answerFormDto.getImageList());

        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                image.setAnswer(answer);
                imageRepository.save(image);
                log.info("이미지 저장 완료");
            }
        }
        return savedAnswer;
    }

    /**
     * 답변 작성자 찾기
     */
    public String findWriter(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
        return answer.getMember().getLoginId();
    }

    /**
     * 답변 상세 정보
     */
    public AnswerDto findAnswerDetail(Long answerId) {
        Answer answer = answerRepository.findAnswerDetail(answerId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
        List<ImageDto> imageDtoList = imageRepository.findByAnswerId(answerId).stream()
                .map(image -> new ImageDto(image))
                .collect(Collectors.toList());

//        Long heartNum = heartRepository.findHearNumByAnswerId(answerId);

        AnswerDto answerDto = AnswerDto.builder()
                .answerId(answer.getId())
                .content(answer.getContent())
                .createdBy(answer.getMember().getNickname())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .imageDtoList(imageDtoList)
//                .heartNum(heartNum)
                .build();
        return answerDto;
    }

    /**
     * 게시글에 해당하는 답변 리스트 (게시글 상세에서 사용)
     */
    @Transactional(readOnly = true)
    public List<AnswerDto> findByPostId(Long postId) {
//        List<AnswerDto> answerDtoList = answerRepository.findByPostIdWithCommentsAndChildCommentsAndImagesAndMember(postId).stream()
//                .map(answer -> new AnswerDto(answer))
//                .collect(Collectors.toList());


        List<Answer> answerList = answerRepository.findAnswersByPostId(postId);
        List<Long> answerIds = answerList.stream().map(Answer::getId).collect(Collectors.toList());

        List<Comment> commentList = commentRepository.findCommentsByAnswerIds(answerIds);

        Map<Long, List<Comment>> commentMap = commentList.stream()
                .filter(comment -> comment.getParent() == null) // parent 필드가 null이 아닌 경우만 필터링
                .collect(Collectors.groupingBy(comment -> comment.getAnswer().getId()));

        log.info("1");
        for (Answer answer : answerList) {
            Long answerId = answer.getId();
            List<Comment> answerComments = commentMap.getOrDefault(answerId, Collections.emptyList());
            answer.setComments(answerComments);
        }
        log.info("2");
        List<AnswerDto> answerDtoList = answerList.stream()
                .map(answer -> new AnswerDto(answer))
                .collect(Collectors.toList());
        log.info("3");
        return answerDtoList;
    }

    /**
     * 답변 업데이트
     * 이미지는 추가한 이미지만 받아와서 저장
     * 삭제 시 비동기 처리 할 생각
     */
    public Long updateAnswer(Long answerId, AnswerFormDto answerFormDto) throws IOException {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));

        //답변 업데이트
        answer.setContent(answerFormDto.getContent());
        //수정하며 추가한 사진 파일 업로드
        List<Image> imageList = imageService.uploadFile(answerFormDto.getImageList());

        //파일이 있다면 db 저장
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                image.setAnswer(answer); //연관관계 설정
                imageRepository.save(image);
                log.info("이미지 저장 완료");
            }
        }
        return answer.getId();
    }


    /**
     * 포스트에 종속된 답변 전부 삭제 (승지: 댓글 삭제 추가)
     */
    public void deleteByPostId(Long postId) {
        List<Answer> answerList = answerRepository.findByPostId(postId);

        //로컬에 있는 이미지 파일들 삭제
        for (Answer answer : answerList) {
            imageService.deleteFile(answer.getImageList());
            /**
             * 이 부분 잘 모르겠음 굳이 반복문을 돌면서 자식 삭제하고 부모를 삭제해야하나? 그냥 코멘트에 postid 필드도 넣고 한 번에 삭제하면 안되남.?
             */
            commentService.deleteCommentInAnswer(answer.getId());
        }
        imageRepository.deleteImagesByAnswerInPost(postId); //Post 에 등록된 Answer 에 등록된 이미지 파일들 삭제
        answerRepository.deleteAnswersByPostId(postId); // Post 에 등록된 Answer 삭제
    }

    /**
     * 단일 답변 삭제 (승지: 댓글 삭제 추가)
     */
    public void deleteAnswer(Long answerId, Long postId) {
        Answer answer = answerRepository.findAnswerDetail(answerId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));

        imageService.deleteFile(answer.getImageList()); //로컬에 있는 이미지 파일 삭제
        commentService.deleteCommentInAnswer(answerId);

        answerRepository.deleteById(answerId); // answer 삭제 //image 도 고아객체로 삭제

        //게시글에 answer 이 전부 지워졌을 경우
        /**
         * 여기서 가져오는 answerList 가 0인지 확인 필요
         */
        List<Answer> answerList = answerRepository.findByPostId(postId);
        if (answerList.size() == 0) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
            post.setAnswered(false);
        }
    }

    /**
     * 좋아요 추가
     */

    /**
     * 좋아요 삭제
     */
}
