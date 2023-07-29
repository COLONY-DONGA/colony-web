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

//    public RelatedURL(String url) {
//        if (isNotValidRelatedURL(url)) {
//            throw new CustomException(ErrorCode.NOT_VALIDURL);
//        }
//        this.url = url;
//    }
//
//    private boolean isNotValidRelatedURL(String url) {
//        return Objects.isNull(url) || url.length() > MAX_LENGTH || url.isEmpty();
//
//    }

}
