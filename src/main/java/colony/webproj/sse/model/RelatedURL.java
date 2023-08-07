package colony.webproj.sse.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RelatedURL {
    private static final int MAX_LENGTH = 255;

    @Column(nullable = false,length = MAX_LENGTH)
    private String url;

}
