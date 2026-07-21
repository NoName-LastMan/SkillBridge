package BackEnd.SkillBridge.dto.request;

import BackEnd.SkillBridge.entity.SkillLevel;
import jakarta.validation.constraints.NotNull;

public class AddUserSkillRequest {

    @NotNull(message = "Skill ID tidak boleh kosong")
    private Long skillId;

    @NotNull(message = "Level skill tidak boleh kosong")
    private SkillLevel level;

    public AddUserSkillRequest() {}

    public Long getSkillId()     { return skillId; }
    public SkillLevel getLevel() { return level; }

    public void setSkillId(Long skillId)   { this.skillId = skillId; }
    public void setLevel(SkillLevel level) { this.level = level; }
}
