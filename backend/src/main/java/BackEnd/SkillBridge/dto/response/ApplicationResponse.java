package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.Application;
import BackEnd.SkillBridge.entity.ApplicationStatus;

import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private Long projectId;
    private String projectTitle;
    private Long applicantId;
    private String applicantEmail;
    private String applicantName;
    private String applicantNim;
    private String positionApplied;
    private String message;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ApplicationResponse() {}

    public static ApplicationResponse from(Application app, String applicantName, String applicantNim) {
        ApplicationResponse res = new ApplicationResponse();
        res.id = app.getId();
        res.projectId = app.getProject().getId();
        res.projectTitle = app.getProject().getTitle();
        res.applicantId = app.getApplicant().getId();
        res.applicantEmail = app.getApplicant().getEmail();
        res.applicantName = applicantName;
        res.applicantNim = applicantNim;
        res.positionApplied = app.getPositionApplied();
        res.message = app.getMessage();
        res.status = app.getStatus();
        res.createdAt = app.getCreatedAt();
        res.updatedAt = app.getUpdatedAt();
        return res;
    }

    public Long getId()                { return id; }
    public Long getProjectId()         { return projectId; }
    public String getProjectTitle()    { return projectTitle; }
    public Long getApplicantId()       { return applicantId; }
    public String getApplicantEmail()  { return applicantEmail; }
    public String getApplicantName()   { return applicantName; }
    public String getApplicantNim()    { return applicantNim; }
    public String getPositionApplied() { return positionApplied; }
    public String getMessage()         { return message; }
    public ApplicationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
