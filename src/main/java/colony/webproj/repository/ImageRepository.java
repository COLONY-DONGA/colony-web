package colony.webproj.repository;

import colony.webproj.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface ImageRepository extends JpaRepository<Image, Long> {
}
