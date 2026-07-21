package BackEnd.SkillBridge.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 100)
    private String category;

    // ── Constructors ───────────────────────────────────────────────────────
    public Skill() {}

    public Skill(Long id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    // ── Builder ────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String name;
        private String category;

        public Builder name(String n)     { this.name = n; return this; }
        public Builder category(String c) { this.category = c; return this; }

        public Skill build() {
            Skill s = new Skill();
            s.name = name;
            s.category = category;
            return s;
        }
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public Long getId()          { return id; }
    public String getName()      { return name; }
    public String getCategory()  { return category; }

    public void setName(String name)         { this.name = name; }
    public void setCategory(String category) { this.category = category; }
}
