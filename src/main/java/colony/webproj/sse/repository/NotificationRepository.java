package colony.webproj.sse.repository;

import colony.webproj.sse.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.receiver.id = :memberId")
    List<Notification> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select count(n) from Notification n where n.receiver.id = :memberId and n.isRead = false")
    Long countUnReadNotifications(@Param("memberId") Long memberId);
}
