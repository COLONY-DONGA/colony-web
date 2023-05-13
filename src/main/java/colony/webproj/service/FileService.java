package colony.webproj.service;

import colony.webproj.repository.FileRepository;
import colony.webproj.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;


}
