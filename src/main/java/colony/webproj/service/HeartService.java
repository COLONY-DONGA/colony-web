package colony.webproj.service;

import colony.webproj.dto.HeartDto;
import colony.webproj.entity.Answer;
import colony.webproj.entity.Heart;
import colony.webproj.entity.Member;
import colony.webproj.repository.AnswerRepository;
import colony.webproj.repository.HeartRepository;
import colony.webproj.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HeartService {
    private final HeartRepository heartRepository;

    private final MemberRepository memberRepository;

    private final AnswerRepository answerRepository;

    /**
     * 좋아요 추가 메서드 : Heart 엔티티에 AnswerId와 MemberId를 함께 추가하는 것이기 때문에 Add 라는 표현을 씀.
     */
    @Transactional
    public void addHeart(HeartDto heartDto) {
        Optional<Heart> existingHeart = heartRepository.findByAnswerIdAndMemberId(heartDto.getAnswerId(), heartDto.getLoginId());
        if (existingHeart.isPresent()) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        Answer answer = answerRepository.findAnswerDetail(heartDto.getAnswerId())
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
        Member member = memberRepository.findByLoginId(heartDto.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("멤버가 존재하지 않습니다."));

        Heart heart = Heart.builder()
        .answer(answer)
        .member(member)
        .build();

        heartRepository.save(heart);
        answer.increaseLikeCount();
    }

    /**
     * 좋아요 삭제 메서드 : Heart 엔티티에 AnswerId와 MemberId를 함께 추가하는 것이기 때문에 remove 라는 표현을 씀.
     */
    @Transactional
    public void removeHeart(HeartDto heartDto) {

        Answer answer = answerRepository.findAnswerDetail(heartDto.getAnswerId())
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));

        Heart heart = heartRepository.findByAnswerIdAndMemberId(heartDto.getAnswerId(), heartDto.getLoginId())
                .orElseThrow(() -> new IllegalStateException("좋아요를 누른 기록이 없습니다."));

        heartRepository.delete(heart);
        answer.decreaseLikeCount();
    }

}
