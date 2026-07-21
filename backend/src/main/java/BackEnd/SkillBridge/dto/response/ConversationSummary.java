package BackEnd.SkillBridge.dto.response;

import java.time.LocalDateTime;

/**
 * Ringkasan satu percakapan untuk ditampilkan di daftar chat.
 */
public class ConversationSummary {

    // Info lawan bicara
    private Long partnerId;
    private String partnerEmail;
    private String partnerName;
    private String partnerFotoUrl;

    // Pesan terakhir
    private String lastMessageContent;
    private LocalDateTime lastMessageAt;
    private boolean lastMessageFromMe;

    // Pesan yang belum dibaca dari partner ini
    private long unreadCount;

    public ConversationSummary() {}

    public Long getPartnerId()               { return partnerId; }
    public String getPartnerEmail()          { return partnerEmail; }
    public String getPartnerName()           { return partnerName; }
    public String getPartnerFotoUrl()        { return partnerFotoUrl; }
    public String getLastMessageContent()    { return lastMessageContent; }
    public LocalDateTime getLastMessageAt()  { return lastMessageAt; }
    public boolean isLastMessageFromMe()     { return lastMessageFromMe; }
    public long getUnreadCount()             { return unreadCount; }

    public void setPartnerId(Long id)                       { this.partnerId = id; }
    public void setPartnerEmail(String email)               { this.partnerEmail = email; }
    public void setPartnerName(String name)                 { this.partnerName = name; }
    public void setPartnerFotoUrl(String url)               { this.partnerFotoUrl = url; }
    public void setLastMessageContent(String content)       { this.lastMessageContent = content; }
    public void setLastMessageAt(LocalDateTime dt)          { this.lastMessageAt = dt; }
    public void setLastMessageFromMe(boolean fromMe)        { this.lastMessageFromMe = fromMe; }
    public void setUnreadCount(long count)                  { this.unreadCount = count; }
}
