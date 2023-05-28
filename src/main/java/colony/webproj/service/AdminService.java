package colony.webproj.service;

import colony.webproj.dto.MemberMangeDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.Role;
import colony.webproj.entity.type.SearchType;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.MemberRepositoryCustom;
import colony.webproj.repository.PostRepository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * 회원 정보
     */
    public Page<MemberMangeDto> findMemberInfo(Pageable pageable, SearchType searchType, String searchValue) {
        return memberRepository.findAllMemberInfo(pageable, searchType, searchValue);
    }


}
