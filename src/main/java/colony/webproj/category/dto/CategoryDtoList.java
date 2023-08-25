package colony.webproj.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter@Setter
public class CategoryDtoList {
    private List<CategoryDto> categoryDtoList;
    private Long presentCategoryId;
    private String presentCategoryName;

    public CategoryDtoList(List<CategoryDto> categoryDtoList, Long presentCategoryId){
        this.categoryDtoList = categoryDtoList;
        this.presentCategoryId = presentCategoryId;
        this.presentCategoryName=findCategoryNameById(presentCategoryId);
    }

    private String findCategoryNameById(Long id) {
        Optional<CategoryDto> category = categoryDtoList.stream()
                .filter(dto -> dto.getId().equals(id))
                .findFirst();

        return category.isPresent() ? category.get().getCategoryName() : null;
    }
}
