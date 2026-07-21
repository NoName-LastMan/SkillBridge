package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "nama_lengkap", length = 150)
    private String namaLengkap;

    @Column(name = "nim", length = 20)
    private String nim;

    @Column(name = "prodi", length = 100)
    private String prodi;

    @Column(name = "angkatan", length = 10)
    private String angkatan;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "whatsapp", length = 20)
    private String whatsapp;

    @Column(name = "instagram", length = 100)
    private String instagram;

    @Column(name = "linkedin", length = 200)
    private String linkedin;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_privacy", nullable = false)
    private ContactPrivacy contactPrivacy = ContactPrivacy.PUBLIC;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSkill> userSkills = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ── Constructors ───────────────────────────────────────────────────────
    public Profile() {}

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private User user;
        private ContactPrivacy contactPrivacy = ContactPrivacy.PUBLIC;

        public Builder user(User u)                     { this.user = u; return this; }
        public Builder contactPrivacy(ContactPrivacy c) { this.contactPrivacy = c; return this; }

        public Profile build() {
            Profile p = new Profile();
            p.user = user;
            p.contactPrivacy = contactPrivacy;
            return p;
        }
    }

    // ── JPA callbacks ─────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public Long getId()                  { return id; }
    public User getUser()                { return user; }
    public String getNamaLengkap()       { return namaLengkap; }
    public String getNim()               { return nim; }
    public String getProdi()             { return prodi; }
    public String getAngkatan()          { return angkatan; }
    public String getBio()               { return bio; }
    public String getFotoUrl()           { return fotoUrl; }
    public String getWhatsapp()          { return whatsapp; }
    public String getInstagram()         { return instagram; }
    public String getLinkedin()          { return linkedin; }
    public ContactPrivacy getContactPrivacy() { return contactPrivacy; }
    public List<UserSkill> getUserSkills()    { return userSkills; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public LocalDateTime getUpdatedAt()   { return updatedAt; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setUser(User user)                       { this.user = user; }
    public void setNamaLengkap(String namaLengkap)       { this.namaLengkap = namaLengkap; }
    public void setNim(String nim)                       { this.nim = nim; }
    public void setProdi(String prodi)                   { this.prodi = prodi; }
    public void setAngkatan(String angkatan)             { this.angkatan = angkatan; }
    public void setBio(String bio)                       { this.bio = bio; }
    public void setFotoUrl(String fotoUrl)               { this.fotoUrl = fotoUrl; }
    public void setWhatsapp(String whatsapp)             { this.whatsapp = whatsapp; }
    public void setInstagram(String instagram)           { this.instagram = instagram; }
    public void setLinkedin(String linkedin)             { this.linkedin = linkedin; }
    public void setContactPrivacy(ContactPrivacy cp)     { this.contactPrivacy = cp; }
}
