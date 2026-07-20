package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Relasi 1-to-1 dengan User ──────────────────────────────────────────
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    // ── Biodata ────────────────────────────────────────────────────────────
    @Column(name = "nama_lengkap", length = 150)
    private String namaLengkap;

    @Column(unique = true, length = 20)
    private String nim;

    @Column(length = 100)
    private String prodi;

    @Column(length = 50)
    private String angkatan;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    // ── Kontak ─────────────────────────────────────────────────────────────
    @Column(length = 20)
    private String whatsapp;

    @Column(length = 100)
    private String instagram;

    @Column(length = 150)
    private String linkedin;

    // ── Pengaturan Privasi Kontak ──────────────────────────────────────────
    // PUBLIC  → semua user dapat melihat kontak
    // PRIVATE → user lain harus kirim request & disetujui dulu
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_privacy", nullable = false)
    @Builder.Default
    private ContactPrivacy contactPrivacy = ContactPrivacy.PUBLIC;

    // ── Skills ─────────────────────────────────────────────────────────────
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserSkill> userSkills = new ArrayList<>();

    // ── Timestamps ─────────────────────────────────────────────────────────
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
