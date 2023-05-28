package colony.webproj.repository;

import colony.webproj.dto.MemberMangeDto;
import colony.webproj.dto.PostDto;
import colony.webproj.entity.Member;
import colony.webproj.entity.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryCustom {
    public Page<MemberMangeDto> findAllMemberInfo(Pageable pageable, SearchType searchType, String searchValue);

    Optional<Member> findMemberWithLikeCount(String loginId);
}
