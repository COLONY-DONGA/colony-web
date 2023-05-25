package colony.webproj.service;

import colony.webproj.dto.AnswerDto;
import colony.webproj.dto.AnswerFormDto;
import colony.webproj.dto.ImageDto;
import colony.webproj.dto.PostDto;
import colony.webproj.entity.Answer;
import colony.webproj.entity.Image;
import colony.webproj.entity.Member;
import colony.webproj.entity.Post;
import colony.webproj.repository.AnswerRepository;
import colony.webproj.repository.ImageRepository;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.PostRepository.PostRepository;
import colony.webproj.repository.PostRepository.PostRepositoryCustom;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
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

    /**
     * 답변 저장
     */
    public Long saveAnswer(Long postId, String loginId, AnswerFormDto answerFormDto) throws IOException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

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

        AnswerDto answerDto = AnswerDto.builder()
                .answerId(answer.getId())
                .content(answer.getContent())
                .createdBy(answer.getMember().getNickname())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .imageDtoList(imageDtoList)
                .build();
        return answerDto;
    }
}
