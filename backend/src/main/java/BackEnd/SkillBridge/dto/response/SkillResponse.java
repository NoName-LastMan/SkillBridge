package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.Skill;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillResponse {

    private Long id;
    private String name;
    private String category;

    public static SkillResponse from(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .build();
    }
}
