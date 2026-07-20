package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.ContactRequest;
import BackEnd.SkillBridge.entity.ContactRequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContactRequestResponse {

    private Long id;

    // Info requester (yang mengirim request)
    private Long requesterId;
    private String requesterName;
    private String requesterEmail;
    private String requesterNim;

    // Info target (yang diminta kontaknya)
    private Long targetId;
    private String targetName;

    private ContactRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ContactRequestResponse from(ContactRequest req) {
        String requesterName = null;
        String requesterNim = null;

        return ContactRequestResponse.builder()
                .id(req.getId())
                .requesterId(req.getRequester().getId())
                .requesterEmail(req.getRequester().getEmail())
                .requesterName(requesterName)
                .requesterNim(requesterNim)
                .targetId(req.getTarget().getId())
                .targetName(null)
                .status(req.getStatus())
                .createdAt(req.getCreatedAt())
                .updatedAt(req.getUpdatedAt())
                .build();
    }
}
