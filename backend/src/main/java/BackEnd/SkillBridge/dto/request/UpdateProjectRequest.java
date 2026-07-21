package BackEnd.SkillBridge.dto.request;

import BackEnd.SkillBridge.entity.ProjectCategory;
import BackEnd.SkillBridge.entity.ProjectStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateProjectRequest {

    @Size(max = 200, message = "Judul proyek maksimal 200 karakter")
    private String title;

    private String description;
    private ProjectCategory category;
    private ProjectStatus status;

    @Min(value = 1, message = "Minimal 1 anggota")
    private Integer maxMembers;

    private String requiredSkills;

    public UpdateProjectRequest() {}

    public String getTitle()            { return title; }
    public String getDescription()      { return description; }
    public ProjectCategory getCategory() { return category; }
    public ProjectStatus getStatus()    { return status; }
    public Integer getMaxMembers()      { return maxMembers; }
    public String getRequiredSkills()   { return requiredSkills; }

    public void setTitle(String title)                  { this.title = title; }
    public void setDescription(String description)      { this.description = description; }
    public void setCategory(ProjectCategory category)   { this.category = category; }
    public void setStatus(ProjectStatus status)         { this.status = status; }
    public void setMaxMembers(Integer maxMembers)       { this.maxMembers = maxMembers; }
    public void setRequiredSkills(String rs)            { this.requiredSkills = rs; }
}
