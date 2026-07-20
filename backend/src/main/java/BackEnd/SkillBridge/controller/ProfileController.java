package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.AddUserSkillRequest;
import BackEnd.SkillBridge.dto.request.UpdatePrivacyRequest;
import BackEnd.SkillBridge.dto.request.UpdateProfileRequest;
import BackEnd.SkillBridge.dto.response.ProfileResponse;
import BackEnd.SkillBridge.dto.response.PublicProfileResponse;
import BackEnd.SkillBridge.dto.response.UserSkillResponse;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.service.ProfileService;
import BackEnd.SkillBridge.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SkillService skillService;

    // ── Profil Saya ────────────────────────────────────────────────────────

    /**
     * GET /api/profile/me
     * Ambil profil lengkap milik sendiri (termasuk semua kontak).
     */
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(profileService.getMyProfile(currentUser));
    }

    /**
     * PUT /api/profile/me
     * Update biodata dan kontak profil.
     */
    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(profileService.updateProfile(currentUser, request));
    }

    /**
     * PUT /api/profile/me/privacy
     * Ubah pengaturan privasi kontak (PUBLIC / PRIVATE).
     */
    @PutMapping("/me/privacy")
    public ResponseEntity<ProfileResponse> updatePrivacy(
            @Valid @RequestBody UpdatePrivacyRequest request) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(profileService.updatePrivacy(currentUser, request));
    }

    // ── Skill Saya ─────────────────────────────────────────────────────────

    /**
     * GET /api/profile/me/skills
     * Ambil daftar skill milik saya.
     */
    @GetMapping("/me/skills")
    public ResponseEntity<List<UserSkillResponse>> getMySkills() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(skillService.getMySkills(currentUser.getId()));
    }

    /**
     * POST /api/profile/me/skills
     * Tambahkan skill ke profil saya.
     * Body: { "skillId": 1, "level": "INTERMEDIATE" }
     */
    @PostMapping("/me/skills")
    public ResponseEntity<UserSkillResponse> addSkill(
            @Valid @RequestBody AddUserSkillRequest request) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(skillService.addSkillToUser(currentUser, request));
    }

    /**
     * DELETE /api/profile/me/skills/{skillId}
     * Hapus skill dari profil saya.
     */
    @DeleteMapping("/me/skills/{skillId}")
    public ResponseEntity<Void> removeSkill(@PathVariable Long skillId) {
        User currentUser = getCurrentUser();
        skillService.removeSkillFromUser(currentUser.getId(), skillId);
        return ResponseEntity.noContent().build();
    }

    // ── Profil Publik User Lain ────────────────────────────────────────────

    /**
     * GET /api/profile/{userId}
     * Lihat profil publik user lain.
     * Kontak hanya tampil sesuai pengaturan privasi.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<PublicProfileResponse> getPublicProfile(
            @PathVariable Long userId) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                profileService.getPublicProfile(userId, currentUser.getId()));
    }

    // ── Helper ─────────────────────────────────────────────────────────────
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
