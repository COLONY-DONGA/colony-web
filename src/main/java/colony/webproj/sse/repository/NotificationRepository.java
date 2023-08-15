package colony.webproj.sse.repository;

import colony.webproj.sse.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.receiver.id = :memberId order by n.createdAt desc")
    List<Notification> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select count(n) from Notification n where n.receiver.id = :memberId and n.isRead = false")
    Long countUnReadNotifications(@Param("memberId") Long memberId);

    @Query("select n from Notification n join fetch n.receiver r where n.id = :notificationId and r.id = :memberId")
    Optional<Notification> findByNotificationIdAndMemberId(@Param("memberId") Long memberId, @Param("notificationId") Long notificationId);

    @Modifying
    @Query("update Notification n set n.isRead = true where n.receiver.id = :memberId")
    void updateReadAllNotification(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from Notification n where n.receiver.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
