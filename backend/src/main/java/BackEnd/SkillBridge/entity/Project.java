package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.OPEN;

    @Column(name = "max_members")
    private Integer maxMembers;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // ── Constructors ───────────────────────────────────────────────────────
    public Project() {}

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String title;
        private String description;
        private ProjectCategory category;
        private ProjectStatus status = ProjectStatus.OPEN;
        private Integer maxMembers;
        private String requiredSkills;
        private User createdBy;

        public Builder title(String t)                  { this.title = t; return this; }
        public Builder description(String d)            { this.description = d; return this; }
        public Builder category(ProjectCategory c)      { this.category = c; return this; }
        public Builder status(ProjectStatus s)          { this.status = s; return this; }
        public Builder maxMembers(Integer m)            { this.maxMembers = m; return this; }
        public Builder requiredSkills(String r)        { this.requiredSkills = r; return this; }
        public Builder createdBy(User u)               { this.createdBy = u; return this; }

        public Project build() {
            Project p = new Project();
            p.title = title; p.description = description; p.category = category;
            p.status = status; p.maxMembers = maxMembers;
            p.requiredSkills = requiredSkills; p.createdBy = createdBy;
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
    public Long getId()                   { return id; }
    public String getTitle()              { return title; }
    public String getDescription()        { return description; }
    public ProjectCategory getCategory()  { return category; }
    public ProjectStatus getStatus()      { return status; }
    public Integer getMaxMembers()        { return maxMembers; }
    public String getRequiredSkills()     { return requiredSkills; }
    public User getCreatedBy()            { return createdBy; }
    public List<Application> getApplications() { return applications; }
    public List<TeamMember> getTeamMembers()   { return teamMembers; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public LocalDateTime getUpdatedAt()   { return updatedAt; }
    public LocalDateTime getDeletedAt()   { return deletedAt; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setTitle(String title)               { this.title = title; }
    public void setDescription(String description)   { this.description = description; }
    public void setCategory(ProjectCategory category){ this.category = category; }
    public void setStatus(ProjectStatus status)      { this.status = status; }
    public void setMaxMembers(Integer maxMembers)    { this.maxMembers = maxMembers; }
    public void setRequiredSkills(String rs)         { this.requiredSkills = rs; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}
