package BackEnd.SkillBridge.dto.request;

import BackEnd.SkillBridge.entity.ContactPrivacy;
import jakarta.validation.constraints.NotNull;

public class UpdatePrivacyRequest {

    @NotNull(message = "Pengaturan privasi tidak boleh kosong")
    private ContactPrivacy contactPrivacy;

    public UpdatePrivacyRequest() {}

    public ContactPrivacy getContactPrivacy() { return contactPrivacy; }
    public void setContactPrivacy(ContactPrivacy contactPrivacy) {
        this.contactPrivacy = contactPrivacy;
    }
}
