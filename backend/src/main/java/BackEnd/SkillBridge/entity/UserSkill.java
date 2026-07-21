package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_skills")
public class UserSkill {

    @EmbeddedId
    private UserSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel level;

    // ── Constructors ───────────────────────────────────────────────────────
    public UserSkill() {}

    public UserSkill(UserSkillId id, User user, Skill skill, SkillLevel level) {
        this.id = id;
        this.user = user;
        this.skill = skill;
        this.level = level;
    }

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UserSkillId id;
        private User user;
        private Skill skill;
        private SkillLevel level;

        public Builder id(UserSkillId id)   { this.id = id; return this; }
        public Builder user(User u)         { this.user = u; return this; }
        public Builder skill(Skill s)       { this.skill = s; return this; }
        public Builder level(SkillLevel l)  { this.level = l; return this; }

        public UserSkill build() {
            return new UserSkill(id, user, skill, level);
        }
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public UserSkillId getId()  { return id; }
    public User getUser()       { return user; }
    public Skill getSkill()     { return skill; }
    public SkillLevel getLevel() { return level; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setId(UserSkillId id)     { this.id = id; }
    public void setLevel(SkillLevel l)    { this.level = l; }
}
