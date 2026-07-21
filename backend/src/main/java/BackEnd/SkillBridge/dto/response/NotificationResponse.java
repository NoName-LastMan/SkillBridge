package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.Notification;
import BackEnd.SkillBridge.entity.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private NotificationType type;
    private String title;
    private String message;
    private boolean isRead;
    private Long referenceId;
    private String referenceType;
    private LocalDateTime createdAt;

    public NotificationResponse() {}

    public static NotificationResponse from(Notification n) {
        NotificationResponse res = new NotificationResponse();
        res.id = n.getId();
        res.type = n.getType();
        res.title = n.getTitle();
        res.message = n.getMessage();
        res.isRead = n.isRead();
        res.referenceId = n.getReferenceId();
        res.referenceType = n.getReferenceType();
        res.createdAt = n.getCreatedAt();
        return res;
    }

    public Long getId()                { return id; }
    public NotificationType getType()  { return type; }
    public String getTitle()           { return title; }
    public String getMessage()         { return message; }
    public boolean isRead()            { return isRead; }
    public Long getReferenceId()       { return referenceId; }
    public String getReferenceType()   { return referenceType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
