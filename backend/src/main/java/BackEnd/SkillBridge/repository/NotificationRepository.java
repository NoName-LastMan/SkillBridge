package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Semua notifikasi user, terbaru di atas
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Hanya notifikasi yang belum dibaca
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // Jumlah notifikasi belum dibaca (untuk badge counter)
    long countByUserIdAndIsReadFalse(Long userId);

    // Mark semua notifikasi user sebagai sudah dibaca
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
    int markAllAsRead(@Param("userId") Long userId);
}
