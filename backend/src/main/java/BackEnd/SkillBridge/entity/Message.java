package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Constructors ───────────────────────────────────────────────────────
    public Message() {}

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private User sender;
        private User receiver;
        private String content;

        public Builder sender(User s)    { this.sender = s; return this; }
        public Builder receiver(User r)  { this.receiver = r; return this; }
        public Builder content(String c) { this.content = c; return this; }

        public Message build() {
            Message m = new Message();
            m.sender = sender; m.receiver = receiver; m.content = content;
            return m;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public Long getId()                  { return id; }
    public User getSender()              { return sender; }
    public User getReceiver()            { return receiver; }
    public String getContent()           { return content; }
    public boolean isRead()              { return isRead; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setRead(boolean read)    { this.isRead = read; }
}
