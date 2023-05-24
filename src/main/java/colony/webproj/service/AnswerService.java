package colony.webproj.service;

import colony.webproj.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
}
