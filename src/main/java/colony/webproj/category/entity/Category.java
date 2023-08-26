package colony.webproj.category.entity;

import colony.webproj.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String categoryName;

    private Boolean isDefault;
}
