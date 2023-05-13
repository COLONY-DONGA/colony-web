package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Image {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String storeImageName;   // 저장위치
    private String originImageName;  // 파일이름

    @ManyToOne(fetch = FetchType.LAZY) // 멤버 셀렉은 나중에.
    @JoinColumn(name = "post_id")
    private Post post;

}
