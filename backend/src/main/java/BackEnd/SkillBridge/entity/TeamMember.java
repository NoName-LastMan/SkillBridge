package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "team_members",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_project_user",
        columnNames = {"project_id", "user_id"}
    )
)
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "team_role", length = 100)
    private String teamRole;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    // ── Constructors ───────────────────────────────────────────────────────
    public TeamMember() {}

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Project project;
        private User user;
        private String teamRole;

        public Builder project(Project p)    { this.project = p; return this; }
        public Builder user(User u)          { this.user = u; return this; }
        public Builder teamRole(String r)    { this.teamRole = r; return this; }

        public TeamMember build() {
            TeamMember tm = new TeamMember();
            tm.project = project; tm.user = user; tm.teamRole = teamRole;
            return tm;
        }
    }

    // ── JPA callbacks ─────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public Long getId()           { return id; }
    public Project getProject()   { return project; }
    public User getUser()         { return user; }
    public String getTeamRole()   { return teamRole; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
}
