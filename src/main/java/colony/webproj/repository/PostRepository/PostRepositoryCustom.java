package colony.webproj.repository.PostRepository;

import colony.webproj.dto.PostDto;
import colony.webproj.dto.PostManageDto;
import colony.webproj.entity.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostDto> findPostDtoList(SearchType searchType, String searchValue, Boolean answered, String sortBy, Pageable pageable);
    Page<PostManageDto> findPostDtoListAdmin(SearchType searchType, String searchValue, Pageable pageable);
}
