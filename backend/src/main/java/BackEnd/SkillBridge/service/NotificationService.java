package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.response.NotificationResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // ─────────────────────────────────────────────────────────────────────
    // CREATE — dipanggil oleh service lain
    // ─────────────────────────────────────────────────────────────────────

    @Transactional
    public void notify(User recipient, NotificationType type,
                       String title, String message,
                       Long referenceId, String referenceType) {
        Notification n = Notification.builder()
                .user(recipient)
                .type(type)
                .title(title)
                .message(message)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .build();
        notificationRepository.save(n);
    }

    // ── Shortcut methods untuk setiap event ───────────────────────────────

    /** Notif ke ketua tim: ada lamaran masuk */
    @Transactional
    public void notifyApplicationReceived(User projectOwner, String applicantName,
                                           String projectTitle, Long applicationId) {
        notify(projectOwner, NotificationType.APPLICATION_RECEIVED,
                "Lamaran Masuk",
                String.format("%s melamar ke proyek '%s'", applicantName, projectTitle),
                applicationId, "APPLICATION");
    }

    /** Notif ke pelamar: lamarannya diterima */
    @Transactional
    public void notifyApplicationAccepted(User applicant, String projectTitle, Long projectId) {
        notify(applicant, NotificationType.APPLICATION_ACCEPTED,
                "Lamaran Diterima 🎉",
                String.format("Selamat! Lamaranmu ke proyek '%s' telah diterima.", projectTitle),
                projectId, "PROJECT");
    }

    /** Notif ke pelamar: lamarannya ditolak */
    @Transactional
    public void notifyApplicationRejected(User applicant, String projectTitle, Long projectId) {
        notify(applicant, NotificationType.APPLICATION_REJECTED,
                "Lamaran Tidak Diterima",
                String.format("Lamaranmu ke proyek '%s' tidak diterima kali ini.", projectTitle),
                projectId, "PROJECT");
    }

    /** Notif ke target: ada permintaan akses kontak */
    @Transactional
    public void notifyContactRequestReceived(User target, String requesterName, Long requestId) {
        notify(target, NotificationType.CONTACT_REQUEST_RECEIVED,
                "Permintaan Kontak",
                String.format("%s ingin melihat kontak Anda.", requesterName),
                requestId, "CONTACT_REQUEST");
    }

    /** Notif ke requester: permintaannya disetujui */
    @Transactional
    public void notifyContactRequestApproved(User requester, String targetName, Long targetUserId) {
        notify(requester, NotificationType.CONTACT_REQUEST_APPROVED,
                "Permintaan Kontak Disetujui ✅",
                String.format("%s menyetujui permintaan kontakmu. Kamu sekarang bisa melihat kontaknya.", targetName),
                targetUserId, "USER");
    }

    /** Notif ke requester: permintaannya ditolak */
    @Transactional
    public void notifyContactRequestRejected(User requester, String targetName) {
        notify(requester, NotificationType.CONTACT_REQUEST_REJECTED,
                "Permintaan Kontak Ditolak",
                String.format("%s tidak menyetujui permintaan kontakmu.", targetName),
                null, null);
    }

    /** Notif ke user: kontak otomatis terbuka setelah lamaran diterima */
    @Transactional
    public void notifyContactAutoApproved(User user, String otherUserName, Long otherUserId) {
        notify(user, NotificationType.CONTACT_AUTO_APPROVED,
                "Akses Kontak Otomatis Dibuka 🤝",
                String.format("Karena bergabung dalam tim yang sama, kamu kini bisa melihat kontak %s.", otherUserName),
                otherUserId, "USER");
    }

    /** Notif ke penerima: ada pesan masuk */
    @Transactional
    public void notifyNewMessage(User receiver, String senderName, Long senderId) {
        notify(receiver, NotificationType.NEW_MESSAGE,
                "Pesan Baru 💬",
                String.format("Kamu mendapat pesan baru dari %s.", senderName),
                senderId, "USER");
    }

    // ─────────────────────────────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public NotificationResponse markAsRead(Long notificationId, Long userId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notifikasi tidak ditemukan"));
        if (!n.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bukan notifikasi Anda");
        }
        n.setRead(true);
        return NotificationResponse.from(notificationRepository.save(n));
    }

    @Transactional
    public int markAllAsRead(Long userId) {
        return notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notifikasi tidak ditemukan"));
        if (!n.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bukan notifikasi Anda");
        }
        notificationRepository.delete(n);
    }
}
