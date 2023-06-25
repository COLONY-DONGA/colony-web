package colony.webproj.repository;


import colony.webproj.dto.MemberMangeDto;
import colony.webproj.dto.MemberWithLikesDto;
import colony.webproj.entity.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryCustom {
    public Page<MemberManageDto> findAllMemberInfo(Pageable pageable, SearchType searchType, String searchValue);

    public MemberWithLikesDto findMemberWithLikeCount(String loginId);
}
