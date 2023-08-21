package colony.webproj.category.dto;

import colony.webproj.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank
    private String categoryName;

    public static CategoryDto from(Category entity){
        return new CategoryDto(
                entity.getId(),
        entity.getCategoryName()
                );
    }
}
