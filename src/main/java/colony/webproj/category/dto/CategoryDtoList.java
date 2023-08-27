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
    private String presentCategory;

    public CategoryDtoList(List<CategoryDto> categoryDtoList, String presentCategory){
        this.categoryDtoList = categoryDtoList;
        this.presentCategory = presentCategory;
    }
}
