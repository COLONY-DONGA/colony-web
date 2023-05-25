package colony.webproj.repository.PostRepository;

import colony.webproj.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    //  Repository에서 반환할 때 바로 Page<Post>로 받아옴.
    Page<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title, Pageable pageable);

    Page<Post> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String content, Pageable pageable);

    Page<Post> findByMember_NicknameContainingOrderByCreatedAtDesc(String nickname, Pageable pageable);

    @Query("select p from Post p left join fetch p.imageList i join p.member m where p.id= :postId")
    Optional<Post> findPostDetail(@Param("postId") Long postId);

    Page<Post> findByMember_LoginIdContainingOrderByCreatedAtDesc(String loginId, Pageable pageable);


    Page<Post> findByMember_NameContainingOrderByCreatedAtDesc(String searchKeyword, Pageable pageable);
}
