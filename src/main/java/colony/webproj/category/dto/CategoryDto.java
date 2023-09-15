package colony.webproj.category.dto;

import colony.webproj.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thymeleaf.engine.ISSEThrottledTemplateWriterControl;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    @NotBlank
    private String categoryName;

    public CategoryDto(Category defaultCategory) {
        this.id = defaultCategory.getId();
        this.categoryName = defaultCategory.getCategoryName();
    }

    public static CategoryDto from(Category entity) {
        return new CategoryDto(
                entity.getId(),
                entity.getCategoryName()
        );
    }


}
