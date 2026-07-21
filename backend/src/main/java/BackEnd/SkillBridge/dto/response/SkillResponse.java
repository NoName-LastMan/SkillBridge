package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.Skill;

public class SkillResponse {

    private Long id;
    private String name;
    private String category;

    public SkillResponse() {}

    public static SkillResponse from(Skill skill) {
        SkillResponse res = new SkillResponse();
        res.id = skill.getId();
        res.name = skill.getName();
        res.category = skill.getCategory();
        return res;
    }

    public Long getId()         { return id; }
    public String getName()     { return name; }
    public String getCategory() { return category; }
}
