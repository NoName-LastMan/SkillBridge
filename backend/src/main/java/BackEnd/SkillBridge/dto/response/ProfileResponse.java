package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.ContactPrivacy;
import BackEnd.SkillBridge.entity.Profile;

import java.util.List;

public class ProfileResponse {

    private Long id;
    private Long userId;
    private String email;
    private String namaLengkap;
    private String nim;
    private String prodi;
    private String angkatan;
    private String bio;
    private String fotoUrl;
    private String whatsapp;
    private String instagram;
    private String linkedin;
    private ContactPrivacy contactPrivacy;
    private List<UserSkillResponse> skills;

    public ProfileResponse() {}

    public static ProfileResponse from(Profile profile, List<UserSkillResponse> skills) {
        ProfileResponse res = new ProfileResponse();
        res.id = profile.getId();
        res.userId = profile.getUser().getId();
        res.email = profile.getUser().getEmail();
        res.namaLengkap = profile.getNamaLengkap();
        res.nim = profile.getNim();
        res.prodi = profile.getProdi();
        res.angkatan = profile.getAngkatan();
        res.bio = profile.getBio();
        res.fotoUrl = profile.getFotoUrl();
        res.whatsapp = profile.getWhatsapp();
        res.instagram = profile.getInstagram();
        res.linkedin = profile.getLinkedin();
        res.contactPrivacy = profile.getContactPrivacy();
        res.skills = skills;
        return res;
    }

    public Long getId()                    { return id; }
    public Long getUserId()                { return userId; }
    public String getEmail()               { return email; }
    public String getNamaLengkap()         { return namaLengkap; }
    public String getNim()                 { return nim; }
    public String getProdi()               { return prodi; }
    public String getAngkatan()            { return angkatan; }
    public String getBio()                 { return bio; }
    public String getFotoUrl()             { return fotoUrl; }
    public String getWhatsapp()            { return whatsapp; }
    public String getInstagram()           { return instagram; }
    public String getLinkedin()            { return linkedin; }
    public ContactPrivacy getContactPrivacy() { return contactPrivacy; }
    public List<UserSkillResponse> getSkills() { return skills; }
}
