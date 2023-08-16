package colony.webproj.repository.memberRepository;


import colony.webproj.dto.MemberManageDto;
import colony.webproj.dto.MemberPageDto;
import colony.webproj.dto.MyPageDto;
import colony.webproj.entity.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryCustom {
    public Page<MemberManageDto> findAllMemberInfo(Pageable pageable, SearchType searchType, String searchValue);

    public MyPageDto findMemberWithLikeCount(String loginId);

    MemberPageDto findMemberInfo(String loginId);
}
