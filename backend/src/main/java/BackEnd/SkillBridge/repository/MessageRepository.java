package BackEnd.SkillBridge.repository;

import BackEnd.SkillBridge.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Semua pesan antara dua user (conversation), diurutkan dari terlama ke terbaru.
     */
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("userId1") Long userId1,
                                    @Param("userId2") Long userId2);

    /**
     * Daftar ID user yang pernah saya ajak bicara (untuk conversation list).
     * Gunakan UNION antara sender dan receiver.
     */
    @Query("SELECT DISTINCT m.receiver.id FROM Message m WHERE m.sender.id = :userId " +
           "UNION SELECT DISTINCT m.sender.id FROM Message m WHERE m.receiver.id = :userId")
    List<Long> findConversationPartnerIds(@Param("userId") Long userId);

    /**
     * Pesan terbaru dalam satu conversation (untuk preview di conversation list).
     */
    @Query("SELECT m FROM Message m WHERE " +
           "((m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1)) " +
           "ORDER BY m.createdAt DESC LIMIT 1")
    List<Message> findLastMessage(@Param("userId1") Long userId1,
                                   @Param("userId2") Long userId2);

    /**
     * Tandai semua pesan dari pengirim tertentu ke receiver sebagai sudah dibaca.
     */
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.sender.id = :senderId " +
           "AND m.receiver.id = :receiverId AND m.isRead = false")
    int markAsRead(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    // Hitung pesan yang belum dibaca oleh user (dari siapa saja)
    long countByReceiverIdAndIsReadFalse(Long receiverId);
}
