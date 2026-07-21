package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.TeamMember;

import java.time.LocalDateTime;

public class TeamMemberResponse {

    private Long id;
    private Long projectId;
    private Long userId;
    private String userEmail;
    private String userName;
    private String userNim;
    private String teamRole;
    private LocalDateTime joinedAt;

    public TeamMemberResponse() {}

    public static TeamMemberResponse from(TeamMember member, String userName, String userNim) {
        TeamMemberResponse res = new TeamMemberResponse();
        res.id = member.getId();
        res.projectId = member.getProject().getId();
        res.userId = member.getUser().getId();
        res.userEmail = member.getUser().getEmail();
        res.userName = userName;
        res.userNim = userNim;
        res.teamRole = member.getTeamRole();
        res.joinedAt = member.getJoinedAt();
        return res;
    }

    public Long getId()             { return id; }
    public Long getProjectId()      { return projectId; }
    public Long getUserId()         { return userId; }
    public String getUserEmail()    { return userEmail; }
    public String getUserName()     { return userName; }
    public String getUserNim()      { return userNim; }
    public String getTeamRole()     { return teamRole; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
}
