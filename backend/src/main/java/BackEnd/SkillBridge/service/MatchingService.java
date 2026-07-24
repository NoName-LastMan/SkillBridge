package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.response.CollaboratorMatchResult;
import BackEnd.SkillBridge.dto.response.MatchProjectResult;
import BackEnd.SkillBridge.dto.response.UserSkillResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private UserRepository userRepository;

    // ─────────────────────────────────────────────────────────────────────
    // 1. PROYEK YANG COCOK UNTUK SAYA
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Kembalikan semua proyek OPEN yang sudah diurutkan berdasarkan
     * matching score dengan skill user saat ini (DESC).
     * Score 0 tetap disertakan agar user bisa melihat semua proyek.
     */
    @Transactional(readOnly = true)
    public List<MatchProjectResult> matchProjectsForUser(User user) {
        List<String> mySkillNames = getUserSkillNames(user.getId());

        return projectRepository.findByStatusAndDeletedAtIsNullOrderByCreatedAtDesc(ProjectStatus.OPEN)
                .stream()
                .map(project -> buildMatchResult(project, mySkillNames, user.getId()))
                .sorted(Comparator.comparingDouble(MatchProjectResult::getMatchScore).reversed())
                .toList();
    }

    /**
     * Hitung matching score untuk satu proyek spesifik.
     */
    @Transactional(readOnly = true)
    public MatchProjectResult matchProjectForUser(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proyek tidak ditemukan"));

        List<String> mySkillNames = getUserSkillNames(user.getId());
        return buildMatchResult(project, mySkillNames, user.getId());
    }

    // ─────────────────────────────────────────────────────────────────────
    // 2. CARI KOLABORATOR UNTUK SEBUAH PROYEK
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Temukan user-user yang skillnya paling cocok dengan kebutuhan proyek.
     * Berguna bagi ketua tim untuk mencari calon anggota.
     * Diurutkan dari match score tertinggi.
     */
    @Transactional(readOnly = true)
    public List<CollaboratorMatchResult> findCollaborators(Long projectId, User requestingUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proyek tidak ditemukan"));

        // Parse skill yang dibutuhkan proyek
        List<String> required = parseRequiredSkills(project.getRequiredSkills());
        if (required.isEmpty()) {
            return List.of();
        }

        // Dapatkan set ID anggota aktif
        Set<Long> memberIds = teamMemberRepository.findByProjectId(projectId)
                .stream()
                .map(m -> m.getUser().getId())
                .collect(Collectors.toSet());

        // Cari semua user yang punya setidaknya satu skill yang dibutuhkan
        return userRepository.findAll()
                .stream()
                .filter(u -> !u.getId().equals(requestingUser.getId())) // exclude diri sendiri
                .map(u -> buildCollaboratorResult(u, required, memberIds))
                .filter(r -> r.getMatchScore() > 0) // hanya tampilkan yang ada kecocokan
                .sorted(Comparator.comparingDouble(CollaboratorMatchResult::getMatchScore).reversed())
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────
    // PRIVATE — CORE ALGORITHM
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Inti algoritma matching:
     * Score = (jumlah skill yang cocok / jumlah total skill dibutuhkan) × 100
     * Cocok ditentukan secara case-insensitive.
     */
    private MatchResult computeMatch(List<String> userSkillNamesLower,
                                      List<String> requiredSkillsLower) {
        if (requiredSkillsLower.isEmpty()) {
            return new MatchResult(0, List.of(), requiredSkillsLower);
        }

        Set<String> userSet = new HashSet<>(userSkillNamesLower);

        List<String> matched = requiredSkillsLower.stream()
                .filter(userSet::contains)
                .toList();

        List<String> missing = requiredSkillsLower.stream()
                .filter(s -> !userSet.contains(s))
                .toList();

        double score = requiredSkillsLower.isEmpty()
                ? 0
                : Math.round((double) matched.size() / requiredSkillsLower.size() * 100.0 * 10) / 10.0;

        return new MatchResult(score, matched, missing);
    }

    private MatchProjectResult buildMatchResult(Project project, List<String> userSkillNames, Long userId) {
        List<String> required = parseRequiredSkills(project.getRequiredSkills());

        List<String> userSkillsLower = userSkillNames.stream()
                .map(String::toLowerCase)
                .toList();
        List<String> requiredLower = required.stream()
                .map(String::toLowerCase)
                .toList();

        MatchResult match = computeMatch(userSkillsLower, requiredLower);

        // Info profil ketua
        String createdByName = profileRepository.findByUserId(project.getCreatedBy().getId())
                .map(p -> p.getNamaLengkap())
                .orElse(null);

        int memberCount = teamMemberRepository.findByProjectId(project.getId()).size();

        MatchProjectResult res = new MatchProjectResult();
        res.setProjectId(project.getId());
        res.setTitle(project.getTitle());
        res.setDescription(project.getDescription());
        res.setCategory(project.getCategory());
        res.setStatus(project.getStatus());
        res.setRequiredSkills(project.getRequiredSkills());
        res.setMaxMembers(project.getMaxMembers());
        res.setCreatedById(project.getCreatedBy().getId());
        res.setCreatedByName(createdByName);
        res.setCreatedByEmail(project.getCreatedBy().getEmail());
        res.setCurrentMemberCount(memberCount);
        res.setCreatedAt(project.getCreatedAt());
        res.setMatchScore(match.score);
        res.setMatchedSkills(match.matched);
        res.setMissingSkills(match.missing);
        res.setTotalRequired(required.size());
        res.setTotalMatched(match.matched.size());
        return res;
    }

    private CollaboratorMatchResult buildCollaboratorResult(User user,
                                                             List<String> requiredLower,
                                                             Set<Long> memberIds) {
        List<UserSkill> userSkills = userSkillRepository.findByUserId(user.getId());
        List<String> userSkillNamesLower = userSkills.stream()
                .map(us -> us.getSkill().getName().toLowerCase())
                .toList();

        MatchResult match = computeMatch(userSkillNamesLower, requiredLower);

        Profile profile = profileRepository.findByUserId(user.getId()).orElse(null);

        List<UserSkillResponse> allSkills = userSkills.stream()
                .map(UserSkillResponse::from)
                .toList();

        CollaboratorMatchResult res = new CollaboratorMatchResult();
        res.setUserId(user.getId());
        res.setEmail(user.getEmail());
        res.setNamaLengkap(profile != null ? profile.getNamaLengkap() : null);
        res.setNim(profile != null ? profile.getNim() : null);
        res.setProdi(profile != null ? profile.getProdi() : null);
        res.setAngkatan(profile != null ? profile.getAngkatan() : null);
        res.setFotoUrl(profile != null ? profile.getFotoUrl() : null);
        res.setMatchScore(match.score);
        res.setMatchedSkills(match.matched);
        res.setAllSkills(allSkills);
        res.setAlreadyMember(memberIds.contains(user.getId()));
        return res;
    }

    // ─────────────────────────────────────────────────────────────────────
    // PRIVATE — HELPERS
    // ─────────────────────────────────────────────────────────────────────

    /** Parse "Java, Spring Boot, React" → ["java", "spring boot", "react"] */
    private List<String> parseRequiredSkills(String requiredSkillsStr) {
        if (requiredSkillsStr == null || requiredSkillsStr.isBlank()) return List.of();
        return Arrays.stream(requiredSkillsStr.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private List<String> getUserSkillNames(Long userId) {
        return userSkillRepository.findByUserId(userId)
                .stream()
                .map(us -> us.getSkill().getName())
                .toList();
    }

    /** Internal value object untuk hasil perhitungan matching */
    private record MatchResult(double score, List<String> matched, List<String> missing) {}
}
