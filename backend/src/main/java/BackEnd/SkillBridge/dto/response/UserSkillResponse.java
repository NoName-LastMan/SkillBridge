package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.SkillLevel;
import BackEnd.SkillBridge.entity.UserSkill;

public class UserSkillResponse {

    private Long skillId;
    private String skillName;
    private String skillCategory;
    private SkillLevel level;

    public UserSkillResponse() {}

    public static UserSkillResponse from(UserSkill userSkill) {
        UserSkillResponse res = new UserSkillResponse();
        res.skillId = userSkill.getSkill().getId();
        res.skillName = userSkill.getSkill().getName();
        res.skillCategory = userSkill.getSkill().getCategory();
        res.level = userSkill.getLevel();
        return res;
    }

    public Long getSkillId()          { return skillId; }
    public String getSkillName()      { return skillName; }
    public String getSkillCategory()  { return skillCategory; }
    public SkillLevel getLevel()      { return level; }
}
