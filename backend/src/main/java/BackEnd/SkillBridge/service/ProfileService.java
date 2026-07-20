package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.request.UpdatePrivacyRequest;
import BackEnd.SkillBridge.dto.request.UpdateProfileRequest;
import BackEnd.SkillBridge.dto.response.ProfileResponse;
import BackEnd.SkillBridge.dto.response.PublicProfileResponse;
import BackEnd.SkillBridge.dto.response.UserSkillResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.ContactRequestRepository;
import BackEnd.SkillBridge.repository.ProfileRepository;
import BackEnd.SkillBridge.repository.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    // ── Ambil profil sendiri (full data) ──────────────────────────────────
    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile(User currentUser) {
        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profil tidak ditemukan"));

        List<UserSkillResponse> skills = getUserSkills(currentUser.getId());
        return ProfileResponse.from(profile, skills);
    }

    // ── Ambil profil publik dengan logika privasi ──────────────────────────
    @Transactional(readOnly = true)
    public PublicProfileResponse getPublicProfile(Long targetUserId, Long requesterId) {
        Profile profile = profileRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profil tidak ditemukan"));

        List<UserSkillResponse> skills = getUserSkills(targetUserId);

        // Pemilik sendiri → tampilkan semua
        if (targetUserId.equals(requesterId)) {
            return PublicProfileResponse.fromPublic(profile, skills);
        }

        // Kontak PUBLIC → langsung tampilkan
        if (profile.getContactPrivacy() == ContactPrivacy.PUBLIC) {
            return PublicProfileResponse.fromPublic(profile, skills);
        }

        // Kontak PRIVATE → cek apakah sudah ada APPROVED request
        boolean isApproved = contactRequestRepository
                .existsByRequesterIdAndTargetIdAndStatus(
                        requesterId, targetUserId, ContactRequestStatus.APPROVED);

        if (isApproved) {
            return PublicProfileResponse.fromPrivateApproved(profile, skills);
        }

        // Cek status request yang sudah dikirim (PENDING / REJECTED / belum ada)
        Optional<ContactRequest> existingRequest = contactRequestRepository
                .findByRequesterIdAndTargetId(requesterId, targetUserId);

        ContactRequestStatus requestStatus = existingRequest
                .map(ContactRequest::getStatus)
                .orElse(null);

        return PublicProfileResponse.fromPrivate(profile, skills, requestStatus);
    }

    // ── Update biodata ─────────────────────────────────────────────────────
    @Transactional
    public ProfileResponse updateProfile(User currentUser, UpdateProfileRequest request) {
        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profil tidak ditemukan"));

        // Cek NIM unik jika diubah
        if (request.getNim() != null &&
            !request.getNim().equals(profile.getNim()) &&
            profileRepository.existsByNim(request.getNim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "NIM sudah digunakan");
        }

        // Partial update — hanya update field yang tidak null
        if (request.getNamaLengkap() != null) profile.setNamaLengkap(request.getNamaLengkap());
        if (request.getNim() != null)          profile.setNim(request.getNim());
        if (request.getProdi() != null)        profile.setProdi(request.getProdi());
        if (request.getAngkatan() != null)     profile.setAngkatan(request.getAngkatan());
        if (request.getBio() != null)          profile.setBio(request.getBio());
        if (request.getFotoUrl() != null)      profile.setFotoUrl(request.getFotoUrl());
        if (request.getWhatsapp() != null)     profile.setWhatsapp(request.getWhatsapp());
        if (request.getInstagram() != null)    profile.setInstagram(request.getInstagram());
        if (request.getLinkedin() != null)     profile.setLinkedin(request.getLinkedin());

        Profile saved = profileRepository.save(profile);
        List<UserSkillResponse> skills = getUserSkills(currentUser.getId());
        return ProfileResponse.from(saved, skills);
    }

    // ── Update pengaturan privasi kontak ──────────────────────────────────
    @Transactional
    public ProfileResponse updatePrivacy(User currentUser, UpdatePrivacyRequest request) {
        Profile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profil tidak ditemukan"));

        profile.setContactPrivacy(request.getContactPrivacy());
        Profile saved = profileRepository.save(profile);
        List<UserSkillResponse> skills = getUserSkills(currentUser.getId());
        return ProfileResponse.from(saved, skills);
    }

    // ── Helper: ambil daftar skill user ───────────────────────────────────
    private List<UserSkillResponse> getUserSkills(Long userId) {
        return userSkillRepository.findByUserId(userId)
                .stream()
                .map(UserSkillResponse::from)
                .toList();
    }
}
