package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.request.SendMessageRequest;
import BackEnd.SkillBridge.dto.response.ConversationSummary;
import BackEnd.SkillBridge.dto.response.MessageResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.MessageRepository;
import BackEnd.SkillBridge.repository.ProfileRepository;
import BackEnd.SkillBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private NotificationService notificationService;

    // ─────────────────────────────────────────────────────────────────────
    // KIRIM PESAN
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Kirim pesan ke user lain.
     * Jika receiver belum pernah dapat notif pesan baru dari sender ini
     * dalam waktu singkat, kirim notifikasi.
     */
    @Transactional
    public MessageResponse sendMessage(User sender, Long receiverId, SendMessageRequest request) {
        if (sender.getId().equals(receiverId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tidak bisa mengirim pesan ke diri sendiri");
        }

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User tidak ditemukan"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .build();

        Message saved = messageRepository.save(message);

        // Kirim notifikasi ke penerima (Isolasi notifikasi jika terjadi error)
        try {
            String senderName = profileRepository.findByUserId(sender.getId())
                    .map(p -> p.getNamaLengkap() != null ? p.getNamaLengkap() : sender.getEmail())
                    .orElse(sender.getEmail());
            notificationService.notifyNewMessage(receiver, senderName, sender.getId());
        } catch (Exception ignored) {
            // Isolasi notifikasi untuk MVP
        }

        return buildMessageResponse(saved);
    }

    // ─────────────────────────────────────────────────────────────────────
    // BACA PERCAKAPAN
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Ambil semua pesan antara current user dan user lain (conversation).
     * Otomatis tandai semua pesan dari partner sebagai sudah dibaca.
     */
    @Transactional
    public List<MessageResponse> getConversation(User currentUser, Long partnerId) {
        // Validasi partner exists
        userRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User tidak ditemukan"));

        // Mark pesan dari partner → current user sebagai sudah dibaca
        messageRepository.markAsRead(partnerId, currentUser.getId());

        return messageRepository.findConversation(currentUser.getId(), partnerId)
                .stream()
                .map(this::buildMessageResponse)
                .toList();
    }

    /**
     * Daftar semua percakapan aktif (conversation list untuk sidebar chat).
     * Diurutkan dari percakapan terbaru.
     */
    @Transactional(readOnly = true)
    public List<ConversationSummary> getConversations(User currentUser) {
        List<Long> partnerIds = messageRepository.findConversationPartnerIds(currentUser.getId());

        return partnerIds.stream()
                .map(partnerId -> buildConversationSummary(currentUser, partnerId))
                .sorted((a, b) -> {
                    if (a.getLastMessageAt() == null) return 1;
                    if (b.getLastMessageAt() == null) return -1;
                    return b.getLastMessageAt().compareTo(a.getLastMessageAt());
                })
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────

    private MessageResponse buildMessageResponse(Message msg) {
        String senderName = profileRepository.findByUserId(msg.getSender().getId())
                .map(p -> p.getNamaLengkap())
                .orElse(null);
        String receiverName = profileRepository.findByUserId(msg.getReceiver().getId())
                .map(p -> p.getNamaLengkap())
                .orElse(null);
        return MessageResponse.from(msg, senderName, receiverName);
    }

    private ConversationSummary buildConversationSummary(User currentUser, Long partnerId) {
        Optional<User> partnerOpt = userRepository.findById(partnerId);
        if (partnerOpt.isEmpty()) return null;

        User partner = partnerOpt.get();
        Profile partnerProfile = profileRepository.findByUserId(partnerId).orElse(null);

        List<Message> lastMessages = messageRepository.findLastMessage(currentUser.getId(), partnerId);
        Message last = lastMessages.isEmpty() ? null : lastMessages.get(0);

        // Hitung pesan belum dibaca dari partner ke current user
        long unread = messageRepository.findConversation(currentUser.getId(), partnerId)
                .stream()
                .filter(m -> m.getReceiver().getId().equals(currentUser.getId()) && !m.isRead())
                .count();

        ConversationSummary cs = new ConversationSummary();
        cs.setPartnerId(partnerId);
        cs.setPartnerEmail(partner.getEmail());
        cs.setPartnerName(partnerProfile != null ? partnerProfile.getNamaLengkap() : null);
        cs.setPartnerFotoUrl(partnerProfile != null ? partnerProfile.getFotoUrl() : null);
        cs.setLastMessageContent(last != null ? last.getContent() : null);
        cs.setLastMessageAt(last != null ? last.getCreatedAt() : null);
        cs.setLastMessageFromMe(last != null && last.getSender().getId().equals(currentUser.getId()));
        cs.setUnreadCount(unread);
        return cs;
    }
}
