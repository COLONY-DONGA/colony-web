package colony.webproj.repository.PostRepository;

import colony.webproj.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    //  Repository에서 반환할 때 바로 Page<Post>로 받아옴.
    Page<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title, Pageable pageable);

    Page<Post> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String content, Pageable pageable);

    Page<Post> findByMember_NicknameContainingOrderByCreatedAtDesc(String nickname, Pageable pageable);
}
