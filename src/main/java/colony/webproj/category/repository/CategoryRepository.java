package colony.webproj.category.repository;

import colony.webproj.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c.categoryName from Category c where c.categoryName=:categoryName")
    Optional<String> findByCategoryName(@Param("categoryName") String categoryName);
}
