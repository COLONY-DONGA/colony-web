package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Image extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String storeImageName;   // 저장이름
    private String originImageName;  // 사용자가 올린 파일 이름
    private String s3Url; // s3 url

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;
}
