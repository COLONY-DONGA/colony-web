package colony.webproj.repository;

import colony.webproj.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);
    List<Image> findByAnswerId(Long answerId);
}
