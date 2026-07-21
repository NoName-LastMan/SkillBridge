package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO untuk moderasi konten proyek oleh admin.
 * Admin wajib memberikan alasan penghapusan proyek.
 */
public class ModerateProjectRequest {

    @NotBlank(message = "Alasan moderasi tidak boleh kosong")
    @Size(max = 500, message = "Alasan moderasi maksimal 500 karakter")
    private String reason;

    // ── Constructors ───────────────────────────────────────────────────────
    public ModerateProjectRequest() {}

    public ModerateProjectRequest(String reason) {
        this.reason = reason;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public String getReason()            { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
