package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "applications",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_project_applicant",
        columnNames = {"project_id", "applicant_id"}
    )
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(name = "position_applied", length = 150)
    private String positionApplied;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ── Constructors ───────────────────────────────────────────────────────
    public Application() {}

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Project project;
        private User applicant;
        private String positionApplied;
        private String message;
        private ApplicationStatus status = ApplicationStatus.PENDING;

        public Builder project(Project p)            { this.project = p; return this; }
        public Builder applicant(User u)             { this.applicant = u; return this; }
        public Builder positionApplied(String pos)   { this.positionApplied = pos; return this; }
        public Builder message(String m)             { this.message = m; return this; }
        public Builder status(ApplicationStatus s)   { this.status = s; return this; }

        public Application build() {
            Application a = new Application();
            a.project = project; a.applicant = applicant;
            a.positionApplied = positionApplied; a.message = message; a.status = status;
            return a;
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
    public Long getId()                      { return id; }
    public Project getProject()              { return project; }
    public User getApplicant()               { return applicant; }
    public String getPositionApplied()       { return positionApplied; }
    public String getMessage()               { return message; }
    public ApplicationStatus getStatus()     { return status; }
    public LocalDateTime getCreatedAt()      { return createdAt; }
    public LocalDateTime getUpdatedAt()      { return updatedAt; }

    public void setStatus(ApplicationStatus status) { this.status = status; }
}
