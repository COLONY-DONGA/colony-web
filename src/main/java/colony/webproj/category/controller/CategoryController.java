package colony.webproj.category.controller;

import colony.webproj.category.dto.CategoryDto;
import colony.webproj.category.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<?> getCategory(){
        List<CategoryDto> categoryDtos = categoryService.getCategories();
        return ResponseEntity.ok(categoryDtos);
    }

    @PostMapping("/category")
    public ResponseEntity<?> insertCategory(@Valid CategoryDto categoryDto){
        categoryService.insertCategories(categoryDto.getCategoryName());
        return ResponseEntity.ok("dd");
    }

    @PostMapping("/edit-category")
    public ResponseEntity<?> updateCategory(@Valid CategoryDto categoryDto){
        categoryService.updateCategory(categoryDto);
        return ResponseEntity.ok("bb");
    }


}
