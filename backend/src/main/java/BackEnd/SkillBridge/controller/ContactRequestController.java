package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.response.ContactRequestResponse;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.service.ContactRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactRequestController {

    @Autowired
    private ContactRequestService contactRequestService;

    /**
     * POST /api/contact-requests/{targetUserId}
     * Kirim request untuk melihat kontak user lain yang PRIVATE.
     */
    @PostMapping("/{targetUserId}")
    public ResponseEntity<ContactRequestResponse> sendRequest(
            @PathVariable Long targetUserId) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                contactRequestService.sendContactRequest(currentUser, targetUserId));
    }

    /**
     * GET /api/contact-requests/incoming
     * Lihat request yang masuk ke saya (status PENDING).
     * Digunakan untuk halaman notifikasi/persetujuan.
     */
    @GetMapping("/incoming")
    public ResponseEntity<List<ContactRequestResponse>> getIncomingRequests() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                contactRequestService.getIncomingRequests(currentUser.getId()));
    }

    /**
     * GET /api/contact-requests/outgoing
     * Lihat request yang sudah saya kirim (semua status).
     */
    @GetMapping("/outgoing")
    public ResponseEntity<List<ContactRequestResponse>> getOutgoingRequests() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                contactRequestService.getOutgoingRequests(currentUser.getId()));
    }

    /**
     * PUT /api/contact-requests/{requestId}/approve
     * Setujui request — hanya bisa dilakukan oleh target (pemilik kontak).
     */
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<ContactRequestResponse> approveRequest(
            @PathVariable Long requestId) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                contactRequestService.approveRequest(requestId, currentUser.getId()));
    }

    /**
     * PUT /api/contact-requests/{requestId}/reject
     * Tolak request — hanya bisa dilakukan oleh target (pemilik kontak).
     */
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<ContactRequestResponse> rejectRequest(
            @PathVariable Long requestId) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                contactRequestService.rejectRequest(requestId, currentUser.getId()));
    }

    // ── Helper ─────────────────────────────────────────────────────────────
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
