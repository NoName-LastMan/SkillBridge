package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.ProjectCategory;
import BackEnd.SkillBridge.entity.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Hasil pencocokan antara skill user dengan kebutuhan satu proyek.
 */
public class MatchProjectResult {

    // ── Info Proyek ────────────────────────────────────────────────────────
    private Long projectId;
    private String title;
    private String description;
    private ProjectCategory category;
    private ProjectStatus status;
    private String requiredSkills;
    private Integer maxMembers;

    // Info ketua tim
    private Long createdById;
    private String createdByName;
    private String createdByEmail;

    private int currentMemberCount;
    private LocalDateTime createdAt;

    // ── Matching Score ─────────────────────────────────────────────────────
    /** Persentase kecocokan: 0–100 */
    private double matchScore;

    /** Skill yang user miliki dan dibutuhkan proyek */
    private List<String> matchedSkills;

    /** Skill yang dibutuhkan proyek tapi belum dimiliki user */
    private List<String> missingSkills;

    private int totalRequired;
    private int totalMatched;

    public MatchProjectResult() {}

    // ── Getters ────────────────────────────────────────────────────────────
    public Long getProjectId()               { return projectId; }
    public String getTitle()                 { return title; }
    public String getDescription()           { return description; }
    public ProjectCategory getCategory()     { return category; }
    public ProjectStatus getStatus()         { return status; }
    public String getRequiredSkills()        { return requiredSkills; }
    public Integer getMaxMembers()           { return maxMembers; }
    public Long getCreatedById()             { return createdById; }
    public String getCreatedByName()         { return createdByName; }
    public String getCreatedByEmail()        { return createdByEmail; }
    public int getCurrentMemberCount()       { return currentMemberCount; }
    public LocalDateTime getCreatedAt()      { return createdAt; }
    public double getMatchScore()            { return matchScore; }
    public List<String> getMatchedSkills()   { return matchedSkills; }
    public List<String> getMissingSkills()   { return missingSkills; }
    public int getTotalRequired()            { return totalRequired; }
    public int getTotalMatched()             { return totalMatched; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setProjectId(Long id)                    { this.projectId = id; }
    public void setTitle(String title)                   { this.title = title; }
    public void setDescription(String desc)              { this.description = desc; }
    public void setCategory(ProjectCategory c)           { this.category = c; }
    public void setStatus(ProjectStatus s)               { this.status = s; }
    public void setRequiredSkills(String rs)             { this.requiredSkills = rs; }
    public void setMaxMembers(Integer m)                 { this.maxMembers = m; }
    public void setCreatedById(Long id)                  { this.createdById = id; }
    public void setCreatedByName(String name)            { this.createdByName = name; }
    public void setCreatedByEmail(String email)          { this.createdByEmail = email; }
    public void setCurrentMemberCount(int count)         { this.currentMemberCount = count; }
    public void setCreatedAt(LocalDateTime dt)           { this.createdAt = dt; }
    public void setMatchScore(double score)              { this.matchScore = score; }
    public void setMatchedSkills(List<String> skills)    { this.matchedSkills = skills; }
    public void setMissingSkills(List<String> skills)    { this.missingSkills = skills; }
    public void setTotalRequired(int total)              { this.totalRequired = total; }
    public void setTotalMatched(int matched)             { this.totalMatched = matched; }
}
