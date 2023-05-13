package colony.webproj.repository;

import colony.webproj.dto.PostDto;
import colony.webproj.entity.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostDto> findPostDtoList(SearchType searchType, String searchValue, Boolean answered, String sortBy, Pageable pageable);
}
