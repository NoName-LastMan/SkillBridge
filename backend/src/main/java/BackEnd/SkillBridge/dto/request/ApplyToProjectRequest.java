package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ApplyToProjectRequest {

    @NotBlank(message = "Posisi yang dilamar tidak boleh kosong")
    @Size(max = 150, message = "Posisi maksimal 150 karakter")
    private String positionApplied;

    @NotBlank(message = "Pesan motivasi tidak boleh kosong")
    private String message;

    public ApplyToProjectRequest() {}

    public String getPositionApplied() { return positionApplied; }
    public String getMessage()         { return message; }

    public void setPositionApplied(String positionApplied) { this.positionApplied = positionApplied; }
    public void setMessage(String message)                 { this.message = message; }
}
