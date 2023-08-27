package colony.webproj.category.service;


import colony.webproj.category.dto.CategoryDto;
import colony.webproj.category.entity.Category;
import colony.webproj.category.repository.CategoryRepository;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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

        if(categoryRepository.findByCategoryName(categoryName).isPresent()){
            throw new CustomException(ErrorCode.CATEGORY_ALREADY_EXIST);
        }

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
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setCategoryName(categoryDto.getCategoryName());
    }
}
