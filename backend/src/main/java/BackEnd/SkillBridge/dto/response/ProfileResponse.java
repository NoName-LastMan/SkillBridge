package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.ContactPrivacy;
import BackEnd.SkillBridge.entity.Profile;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Profil lengkap untuk pemilik akun sendiri.
 * Semua data termasuk kontak tampil tanpa filter.
 */
@Getter
@Builder
public class ProfileResponse {

    private Long id;
    private Long userId;
    private String email;

    // Biodata
    private String namaLengkap;
    private String nim;
    private String prodi;
    private String angkatan;
    private String bio;
    private String fotoUrl;

    // Kontak (tampil semua untuk pemilik)
    private String whatsapp;
    private String instagram;
    private String linkedin;

    // Privasi
    private ContactPrivacy contactPrivacy;

    // Skills
    private List<UserSkillResponse> skills;

    public static ProfileResponse from(Profile profile, List<UserSkillResponse> skills) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .email(profile.getUser().getEmail())
                .namaLengkap(profile.getNamaLengkap())
                .nim(profile.getNim())
                .prodi(profile.getProdi())
                .angkatan(profile.getAngkatan())
                .bio(profile.getBio())
                .fotoUrl(profile.getFotoUrl())
                .whatsapp(profile.getWhatsapp())
                .instagram(profile.getInstagram())
                .linkedin(profile.getLinkedin())
                .contactPrivacy(profile.getContactPrivacy())
                .skills(skills)
                .build();
    }
}
