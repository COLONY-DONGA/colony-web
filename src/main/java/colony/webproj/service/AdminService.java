package colony.webproj.service;

import colony.webproj.dto.MemberManageDto;
import colony.webproj.dto.PostManageDto;
import colony.webproj.entity.type.SearchType;
import colony.webproj.repository.memberRepository.MemberRepository;
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

    /**
     * 회원 정보
     */
    public Page<MemberManageDto> findMemberInfo(Pageable pageable, SearchType searchType, String searchValue) {
        return memberRepository.findAllMemberInfo(pageable, searchType, searchValue);
    }

    public Page<PostManageDto> findPostInfo(SearchType searchType, String searchValue, Pageable pageable) {
        return postRepository.findPostDtoListAdmin(searchType, searchValue, pageable);
    }


}
