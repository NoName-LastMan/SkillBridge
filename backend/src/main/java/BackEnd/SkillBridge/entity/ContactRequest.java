package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_requests")
public class ContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactRequestStatus status = ContactRequestStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ── Constructors ───────────────────────────────────────────────────────
    public ContactRequest() {}

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private User requester;
        private User target;
        private ContactRequestStatus status = ContactRequestStatus.PENDING;

        public Builder requester(User r) { this.requester = r; return this; }
        public Builder target(User t)    { this.target = t; return this; }
        public Builder status(ContactRequestStatus s) { this.status = s; return this; }

        public ContactRequest build() {
            ContactRequest cr = new ContactRequest();
            cr.requester = requester;
            cr.target = target;
            cr.status = status;
            return cr;
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

    // ── Getters & Setters ──────────────────────────────────────────────────
    public Long getId()                    { return id; }
    public User getRequester()             { return requester; }
    public User getTarget()                { return target; }
    public ContactRequestStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt()    { return createdAt; }
    public LocalDateTime getUpdatedAt()    { return updatedAt; }

    public void setStatus(ContactRequestStatus status) { this.status = status; }
}
