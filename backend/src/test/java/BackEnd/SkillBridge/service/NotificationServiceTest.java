package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.response.NotificationResponse;
import BackEnd.SkillBridge.entity.NotificationType;
import BackEnd.SkillBridge.entity.Role;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    private User recipient;

    @BeforeEach
    void setUp() {
        recipient = userRepository.save(User.builder()
                .email("notif.target@unimus.ac.id")
                .password("password123")
                .role(Role.MAHASISWA)
                .isVerified(true)
                .build());
    }

    @Test
    @DisplayName("Kirim notifikasi & ambil daftar notifikasi user")
    void testNotifyAndGetMyNotifications() {
        notificationService.notify(recipient, NotificationType.APPLICATION_ACCEPTED,
                "Lamaran Diterima", "Selamat lamaranmu diterima", 10L, "PROJECT");

        List<NotificationResponse> notifs = notificationService.getMyNotifications(recipient.getId());

        assertEquals(1, notifs.size());
        assertEquals("Lamaran Diterima", notifs.get(0).getTitle());
        assertFalse(notifs.get(0).isRead());
        assertEquals(1, notificationService.countUnread(recipient.getId()));

        // Tandai sudah dibaca
        NotificationResponse readResp = notificationService.markAsRead(notifs.get(0).getId(), recipient.getId());
        assertTrue(readResp.isRead());
        assertEquals(0, notificationService.countUnread(recipient.getId()));
    }
}
