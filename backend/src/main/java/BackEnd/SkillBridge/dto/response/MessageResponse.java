package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.Message;

import java.time.LocalDateTime;

public class MessageResponse {

    private Long id;
    private Long senderId;
    private String senderEmail;
    private String senderName;
    private Long receiverId;
    private String receiverEmail;
    private String receiverName;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;

    public MessageResponse() {}

    public static MessageResponse from(Message msg, String senderName, String receiverName) {
        MessageResponse res = new MessageResponse();
        res.id = msg.getId();
        res.senderId = msg.getSender().getId();
        res.senderEmail = msg.getSender().getEmail();
        res.senderName = senderName;
        res.receiverId = msg.getReceiver().getId();
        res.receiverEmail = msg.getReceiver().getEmail();
        res.receiverName = receiverName;
        res.content = msg.getContent();
        res.isRead = msg.isRead();
        res.createdAt = msg.getCreatedAt();
        return res;
    }

    public Long getId()               { return id; }
    public Long getSenderId()         { return senderId; }
    public String getSenderEmail()    { return senderEmail; }
    public String getSenderName()     { return senderName; }
    public Long getReceiverId()       { return receiverId; }
    public String getReceiverEmail()  { return receiverEmail; }
    public String getReceiverName()   { return receiverName; }
    public String getContent()        { return content; }
    public boolean isRead()           { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
