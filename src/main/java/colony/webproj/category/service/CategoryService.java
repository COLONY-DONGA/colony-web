package colony.webproj.category.service;


import colony.webproj.category.dto.CategoryDto;
import colony.webproj.category.entity.Category;
import colony.webproj.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     *  카테고리 가져오기
     */
    public List<CategoryDto> getCategories(){
        List<CategoryDto> categories = categoryRepository.findAll().stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());

        return categories;
    }

    /**
     * 카테고리 추가
     */
    public void insertCategories(String categoryName){

        Optional<String> usedName = categoryRepository.findByCategoryName(categoryName);

        // 카테고리가 이미 존재하면 어떻게 할지는 일단 보류

        Category category = Category.builder().
                categoryName(categoryName).
                build();

        categoryRepository.save(category);
    }

    /**
     *  카테고리 수정
     */
    public void updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new RuntimeException("fdsfs"));

        category.setCategoryName(categoryDto.getCategoryName());

        categoryRepository.save(category);
    }
}
