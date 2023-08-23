package colony.webproj.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class CategoryDtoList {
    private List<CategoryDto> categoryDtoList;
    private Long presentCategoryId;

    public CategoryDtoList(List<CategoryDto> categoryDtoList, Long presentCategoryId){
        this.categoryDtoList = categoryDtoList;
        this.presentCategoryId = presentCategoryId;
    }
}
