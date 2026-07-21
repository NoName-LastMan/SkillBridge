package BackEnd.SkillBridge.dto.request;

import BackEnd.SkillBridge.entity.ProjectCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateProjectRequest {

    @NotBlank(message = "Judul proyek tidak boleh kosong")
    @Size(max = 200, message = "Judul proyek maksimal 200 karakter")
    private String title;

    private String description;

    @NotNull(message = "Kategori proyek tidak boleh kosong")
    private ProjectCategory category;

    @Min(value = 1, message = "Minimal 1 anggota")
    private Integer maxMembers;

    private String requiredSkills;

    public CreateProjectRequest() {}

    public String getTitle()           { return title; }
    public String getDescription()     { return description; }
    public ProjectCategory getCategory() { return category; }
    public Integer getMaxMembers()     { return maxMembers; }
    public String getRequiredSkills()  { return requiredSkills; }

    public void setTitle(String title)                 { this.title = title; }
    public void setDescription(String description)     { this.description = description; }
    public void setCategory(ProjectCategory category)  { this.category = category; }
    public void setMaxMembers(Integer maxMembers)      { this.maxMembers = maxMembers; }
    public void setRequiredSkills(String rs)           { this.requiredSkills = rs; }
}
