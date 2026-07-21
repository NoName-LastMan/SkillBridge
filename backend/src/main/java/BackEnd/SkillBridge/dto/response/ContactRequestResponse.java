package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.ContactRequest;
import BackEnd.SkillBridge.entity.ContactRequestStatus;

import java.time.LocalDateTime;

public class ContactRequestResponse {

    private Long id;
    private Long requesterId;
    private String requesterName;
    private String requesterEmail;
    private String requesterNim;
    private Long targetId;
    private String targetName;
    private ContactRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContactRequestResponse() {}

    public static ContactRequestResponse from(ContactRequest req) {
        ContactRequestResponse res = new ContactRequestResponse();
        res.id = req.getId();
        res.requesterId = req.getRequester().getId();
        res.requesterEmail = req.getRequester().getEmail();
        res.targetId = req.getTarget().getId();
        res.status = req.getStatus();
        res.createdAt = req.getCreatedAt();
        res.updatedAt = req.getUpdatedAt();
        return res;
    }

    public Long getId()                     { return id; }
    public Long getRequesterId()            { return requesterId; }
    public String getRequesterName()        { return requesterName; }
    public String getRequesterEmail()       { return requesterEmail; }
    public String getRequesterNim()         { return requesterNim; }
    public Long getTargetId()               { return targetId; }
    public String getTargetName()           { return targetName; }
    public ContactRequestStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public LocalDateTime getUpdatedAt()     { return updatedAt; }

    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }
    public void setRequesterNim(String requesterNim)   { this.requesterNim = requesterNim; }
    public void setTargetName(String targetName)       { this.targetName = targetName; }
}
