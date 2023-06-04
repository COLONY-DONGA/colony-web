package colony.webproj.service;

import colony.webproj.dto.MemberManageDto;
import colony.webproj.dto.PostDto;
import colony.webproj.entity.type.SearchType;
import colony.webproj.repository.MemberRepository;
import colony.webproj.repository.PostRepository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<MemberManageDto> findMemberInfo(Pageable pageable, SearchType searchType, String searchValue) {
        return memberRepository.findAllMemberInfo(pageable, searchType, searchValue);
    }

    public Page<PostDto> findPostInfo(SearchType searchType, String searchValue, Pageable pageable) {
        return postRepository.findPostDtoList(searchType, searchValue, null, "createdAt", pageable);
    }


}
