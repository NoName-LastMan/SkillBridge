package BackEnd.SkillBridge.dto.request;

import BackEnd.SkillBridge.entity.ContactPrivacy;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePrivacyRequest {

    @NotNull(message = "Pengaturan privasi kontak tidak boleh kosong (PUBLIC atau PRIVATE)")
    private ContactPrivacy contactPrivacy;
}
