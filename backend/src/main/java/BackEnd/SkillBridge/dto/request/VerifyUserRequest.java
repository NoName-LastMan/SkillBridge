package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO untuk verifikasi atau pencabutan verifikasi akun mahasiswa oleh admin.
 */
public class VerifyUserRequest {

    @NotNull(message = "Status verifikasi tidak boleh null")
    private Boolean isVerified;

    private String reason; // Alasan opsional (terutama jika unverify)

    // ── Constructors ───────────────────────────────────────────────────────
    public VerifyUserRequest() {}

    public VerifyUserRequest(Boolean isVerified, String reason) {
        this.isVerified = isVerified;
        this.reason = reason;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public Boolean getIsVerified()               { return isVerified; }
    public String getReason()                    { return reason; }

    public void setIsVerified(Boolean isVerified){ this.isVerified = isVerified; }
    public void setReason(String reason)         { this.reason = reason; }
}
