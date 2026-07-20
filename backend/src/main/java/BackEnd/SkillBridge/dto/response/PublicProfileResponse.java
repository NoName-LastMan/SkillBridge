package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.ContactPrivacy;
import BackEnd.SkillBridge.entity.ContactRequestStatus;
import BackEnd.SkillBridge.entity.Profile;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Profil publik untuk dilihat user lain.
 * Kontak hanya tampil jika:
 *   - contactPrivacy == PUBLIC, ATAU
 *   - requester sudah punya ContactRequest APPROVED dengan target ini
 *
 * Jika kontak PRIVATE dan belum ada persetujuan:
 *   - kontak field bernilai null
 *   - contactVisible = false (FE bisa tampilkan tombol "Minta Kontak")
 */
@Getter
@Builder
public class PublicProfileResponse {

    private Long userId;
    private String namaLengkap;
    private String nim;
    private String prodi;
    private String angkatan;
    private String bio;
    private String fotoUrl;

    // Kontak — null jika PRIVATE dan belum disetujui
    private String whatsapp;
    private String instagram;
    private String linkedin;

    // Informasi untuk Frontend
    private ContactPrivacy contactPrivacy;
    private boolean contactVisible;        // true = kontak bisa dilihat
    private ContactRequestStatus myRequestStatus; // null jika belum pernah request

    private List<UserSkillResponse> skills;

    public static PublicProfileResponse fromPublic(Profile profile,
                                                    List<UserSkillResponse> skills) {
        return PublicProfileResponse.builder()
                .userId(profile.getUser().getId())
                .namaLengkap(profile.getNamaLengkap())
                .nim(profile.getNim())
                .prodi(profile.getProdi())
                .angkatan(profile.getAngkatan())
                .bio(profile.getBio())
                .fotoUrl(profile.getFotoUrl())
                .whatsapp(profile.getWhatsapp())
                .instagram(profile.getInstagram())
                .linkedin(profile.getLinkedin())
                .contactPrivacy(ContactPrivacy.PUBLIC)
                .contactVisible(true)
                .myRequestStatus(null)
                .skills(skills)
                .build();
    }

    public static PublicProfileResponse fromPrivate(Profile profile,
                                                     List<UserSkillResponse> skills,
                                                     ContactRequestStatus requestStatus) {
        return PublicProfileResponse.builder()
                .userId(profile.getUser().getId())
                .namaLengkap(profile.getNamaLengkap())
                .nim(profile.getNim())
                .prodi(profile.getProdi())
                .angkatan(profile.getAngkatan())
                .bio(profile.getBio())
                .fotoUrl(profile.getFotoUrl())
                // Kontak disembunyikan
                .whatsapp(null)
                .instagram(null)
                .linkedin(null)
                .contactPrivacy(ContactPrivacy.PRIVATE)
                .contactVisible(false)
                .myRequestStatus(requestStatus)
                .skills(skills)
                .build();
    }

    public static PublicProfileResponse fromPrivateApproved(Profile profile,
                                                             List<UserSkillResponse> skills) {
        return PublicProfileResponse.builder()
                .userId(profile.getUser().getId())
                .namaLengkap(profile.getNamaLengkap())
                .nim(profile.getNim())
                .prodi(profile.getProdi())
                .angkatan(profile.getAngkatan())
                .bio(profile.getBio())
                .fotoUrl(profile.getFotoUrl())
                // Kontak tampil karena sudah disetujui
                .whatsapp(profile.getWhatsapp())
                .instagram(profile.getInstagram())
                .linkedin(profile.getLinkedin())
                .contactPrivacy(ContactPrivacy.PRIVATE)
                .contactVisible(true)
                .myRequestStatus(ContactRequestStatus.APPROVED)
                .skills(skills)
                .build();
    }
}
