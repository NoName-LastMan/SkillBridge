package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.ContactPrivacy;
import BackEnd.SkillBridge.entity.ContactRequestStatus;
import BackEnd.SkillBridge.entity.Profile;

import java.util.List;

public class PublicProfileResponse {

    private Long userId;
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
    private boolean contactVisible;
    private ContactRequestStatus myRequestStatus;
    private List<UserSkillResponse> skills;

    public PublicProfileResponse() {}

    public static PublicProfileResponse fromPublic(Profile profile, List<UserSkillResponse> skills) {
        PublicProfileResponse res = new PublicProfileResponse();
        res.userId = profile.getUser().getId();
        res.namaLengkap = profile.getNamaLengkap();
        res.nim = profile.getNim();
        res.prodi = profile.getProdi();
        res.angkatan = profile.getAngkatan();
        res.bio = profile.getBio();
        res.fotoUrl = profile.getFotoUrl();
        res.whatsapp = profile.getWhatsapp();
        res.instagram = profile.getInstagram();
        res.linkedin = profile.getLinkedin();
        res.contactPrivacy = ContactPrivacy.PUBLIC;
        res.contactVisible = true;
        res.skills = skills;
        return res;
    }

    public static PublicProfileResponse fromPrivate(Profile profile,
                                                     List<UserSkillResponse> skills,
                                                     ContactRequestStatus requestStatus) {
        PublicProfileResponse res = new PublicProfileResponse();
        res.userId = profile.getUser().getId();
        res.namaLengkap = profile.getNamaLengkap();
        res.nim = profile.getNim();
        res.prodi = profile.getProdi();
        res.angkatan = profile.getAngkatan();
        res.bio = profile.getBio();
        res.fotoUrl = profile.getFotoUrl();
        res.whatsapp = null;
        res.instagram = null;
        res.linkedin = null;
        res.contactPrivacy = ContactPrivacy.PRIVATE;
        res.contactVisible = false;
        res.myRequestStatus = requestStatus;
        res.skills = skills;
        return res;
    }

    public static PublicProfileResponse fromPrivateApproved(Profile profile,
                                                             List<UserSkillResponse> skills) {
        PublicProfileResponse res = new PublicProfileResponse();
        res.userId = profile.getUser().getId();
        res.namaLengkap = profile.getNamaLengkap();
        res.nim = profile.getNim();
        res.prodi = profile.getProdi();
        res.angkatan = profile.getAngkatan();
        res.bio = profile.getBio();
        res.fotoUrl = profile.getFotoUrl();
        res.whatsapp = profile.getWhatsapp();
        res.instagram = profile.getInstagram();
        res.linkedin = profile.getLinkedin();
        res.contactPrivacy = ContactPrivacy.PRIVATE;
        res.contactVisible = true;
        res.myRequestStatus = ContactRequestStatus.APPROVED;
        res.skills = skills;
        return res;
    }

    public Long getUserId()                       { return userId; }
    public String getNamaLengkap()                { return namaLengkap; }
    public String getNim()                        { return nim; }
    public String getProdi()                      { return prodi; }
    public String getAngkatan()                   { return angkatan; }
    public String getBio()                        { return bio; }
    public String getFotoUrl()                    { return fotoUrl; }
    public String getWhatsapp()                   { return whatsapp; }
    public String getInstagram()                  { return instagram; }
    public String getLinkedin()                   { return linkedin; }
    public ContactPrivacy getContactPrivacy()     { return contactPrivacy; }
    public boolean isContactVisible()             { return contactVisible; }
    public ContactRequestStatus getMyRequestStatus() { return myRequestStatus; }
    public List<UserSkillResponse> getSkills()    { return skills; }
}
