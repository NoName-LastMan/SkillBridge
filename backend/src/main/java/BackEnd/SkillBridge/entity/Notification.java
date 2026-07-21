package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Penerima notifikasi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    // ID entitas terkait (project, application, contact_request, message)
    @Column(name = "reference_id")
    private Long referenceId;

    // Jenis entitas terkait
    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Constructors ───────────────────────────────────────────────────────
    public Notification() {}

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private User user;
        private NotificationType type;
        private String title;
        private String message;
        private Long referenceId;
        private String referenceType;

        public Builder user(User u)                  { this.user = u; return this; }
        public Builder type(NotificationType t)      { this.type = t; return this; }
        public Builder title(String t)               { this.title = t; return this; }
        public Builder message(String m)             { this.message = m; return this; }
        public Builder referenceId(Long id)          { this.referenceId = id; return this; }
        public Builder referenceType(String rt)      { this.referenceType = rt; return this; }

        public Notification build() {
            Notification n = new Notification();
            n.user = user; n.type = type; n.title = title; n.message = message;
            n.referenceId = referenceId; n.referenceType = referenceType;
            return n;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public Long getId()                   { return id; }
    public User getUser()                 { return user; }
    public NotificationType getType()     { return type; }
    public String getTitle()              { return title; }
    public String getMessage()            { return message; }
    public boolean isRead()               { return isRead; }
    public Long getReferenceId()          { return referenceId; }
    public String getReferenceType()      { return referenceType; }
    public LocalDateTime getCreatedAt()   { return createdAt; }

    public void setRead(boolean read)     { this.isRead = read; }
}
