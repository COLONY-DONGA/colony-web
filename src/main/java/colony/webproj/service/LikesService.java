package colony.webproj.service;

import colony.webproj.entity.Answer;
import colony.webproj.entity.Likes;
import colony.webproj.entity.Member;
import colony.webproj.repository.AnswerRepository;
import colony.webproj.repository.LikesRepository;
import colony.webproj.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikesService {
    private final LikesRepository likesRepository;

    private final MemberRepository memberRepository;

    private final AnswerRepository answerRepository;

    /**
     * 좋아요 추가 메서드
     */
    @Transactional
    public boolean addLikes(Long answerId, String loginId) {
        Optional<Likes> existingLikes = likesRepository.findByAnswerIdAndMemberId(answerId, loginId);
        if (existingLikes.isPresent()) {
            return false;
        }

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("답변이 존재하지 않습니다."));
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("멤버가 존재하지 않습니다."));

        Likes likes = Likes.builder()
        .answer(answer)
        .member(member)
        .build();

        likesRepository.save(likes);
        return true;
    }

    /**
     * 좋아요 삭제 메서드
     */
    @Transactional
    public void removeLikes(Long answerId, String loginId) {
        Likes likes = likesRepository.findByAnswerIdAndMemberId(answerId, loginId)
                .orElseThrow(() -> new IllegalStateException("좋아요를 누른 기록이 없습니다."));

        likesRepository.delete(likes);
    }

}
