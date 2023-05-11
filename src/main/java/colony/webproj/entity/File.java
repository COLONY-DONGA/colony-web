package colony.webproj.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor // 생성자?
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class File {
    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    private String storeFileName;
    private String originFileName;

    @ManyToOne(fetch = FetchType.LAZY) // 멤버 셀렉은 나중에.
    @JoinColumn(name = "post_id")
    private Post post;

}
