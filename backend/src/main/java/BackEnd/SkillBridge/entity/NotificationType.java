package BackEnd.SkillBridge.entity;

public enum NotificationType {
    // ── Proyek ─────────────────────────────────────────────────────────────
    APPLICATION_RECEIVED,   // Ketua tim menerima lamaran baru
    APPLICATION_ACCEPTED,   // Pelamar: lamarannya diterima
    APPLICATION_REJECTED,   // Pelamar: lamarannya ditolak

    // ── Kontak ─────────────────────────────────────────────────────────────
    CONTACT_REQUEST_RECEIVED,  // User menerima permintaan akses kontak
    CONTACT_REQUEST_APPROVED,  // Requester: permintaannya disetujui
    CONTACT_REQUEST_REJECTED,  // Requester: permintaannya ditolak
    CONTACT_AUTO_APPROVED,     // Kontak otomatis dibuka setelah lamaran diterima

    // ── Pesan ──────────────────────────────────────────────────────────────
    NEW_MESSAGE                // User menerima pesan baru
}
