package BackEnd.SkillBridge.dto.request;

import BackEnd.SkillBridge.entity.SkillLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserSkillRequest {

    @NotNull(message = "Skill ID tidak boleh kosong")
    private Long skillId;

    @NotNull(message = "Level skill tidak boleh kosong (BEGINNER, INTERMEDIATE, ADVANCED)")
    private SkillLevel level;
}
