package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.Project;
import BackEnd.SkillBridge.entity.ProjectCategory;
import BackEnd.SkillBridge.entity.ProjectStatus;

import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private ProjectCategory category;
    private ProjectStatus status;
    private Integer maxMembers;
    private String requiredSkills;
    private Long createdById;
    private String createdByEmail;
    private String createdByName;
    private int memberCount;
    private int pendingApplicationCount;
    private Boolean hasApplied;
    private Boolean isOwner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectResponse() {}

    public static ProjectResponse from(Project project,
                                       String createdByName,
                                       int memberCount,
                                       int pendingApplicationCount,
                                       Boolean hasApplied,
                                       Boolean isOwner) {
        ProjectResponse res = new ProjectResponse();
        res.id = project.getId();
        res.title = project.getTitle();
        res.description = project.getDescription();
        res.category = project.getCategory();
        res.status = project.getStatus();
        res.maxMembers = project.getMaxMembers();
        res.requiredSkills = project.getRequiredSkills();
        res.createdById = project.getCreatedBy().getId();
        res.createdByEmail = project.getCreatedBy().getEmail();
        res.createdByName = createdByName;
        res.memberCount = memberCount;
        res.pendingApplicationCount = pendingApplicationCount;
        res.hasApplied = hasApplied;
        res.isOwner = isOwner;
        res.createdAt = project.getCreatedAt();
        res.updatedAt = project.getUpdatedAt();
        return res;
    }

    public Long getId()                      { return id; }
    public String getTitle()                 { return title; }
    public String getDescription()           { return description; }
    public ProjectCategory getCategory()     { return category; }
    public ProjectStatus getStatus()         { return status; }
    public Integer getMaxMembers()           { return maxMembers; }
    public String getRequiredSkills()        { return requiredSkills; }
    public Long getCreatedById()             { return createdById; }
    public String getCreatedByEmail()        { return createdByEmail; }
    public String getCreatedByName()         { return createdByName; }
    public int getMemberCount()              { return memberCount; }
    public int getPendingApplicationCount()  { return pendingApplicationCount; }
    public Boolean getHasApplied()           { return hasApplied; }
    public Boolean getIsOwner()              { return isOwner; }
    public LocalDateTime getCreatedAt()      { return createdAt; }
    public LocalDateTime getUpdatedAt()      { return updatedAt; }
}
