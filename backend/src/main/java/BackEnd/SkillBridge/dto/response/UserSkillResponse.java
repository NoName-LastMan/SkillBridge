package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.SkillLevel;
import BackEnd.SkillBridge.entity.UserSkill;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSkillResponse {

    private Long skillId;
    private String skillName;
    private String skillCategory;
    private SkillLevel level;

    public static UserSkillResponse from(UserSkill userSkill) {
        return UserSkillResponse.builder()
                .skillId(userSkill.getSkill().getId())
                .skillName(userSkill.getSkill().getName())
                .skillCategory(userSkill.getSkill().getCategory())
                .level(userSkill.getLevel())
                .build();
    }
}
