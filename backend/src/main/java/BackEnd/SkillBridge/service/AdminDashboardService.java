package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.request.ModerateProjectRequest;
import BackEnd.SkillBridge.dto.request.VerifyUserRequest;
import BackEnd.SkillBridge.dto.response.AdminUserResponse;
import BackEnd.SkillBridge.dto.response.PlatformStatsResponse;
import BackEnd.SkillBridge.dto.response.ProjectResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer untuk Modul Admin Dashboard.
 *
 * Fungsi utama:
 *  1. Statistik aktivitas platform.
 *  2. Manajemen & verifikasi akun mahasiswa.
 *  3. Moderasi konten (hapus proyek).
 */
@Service
public class AdminDashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // ═══════════════════════════════════════════════════════════════════════
    // 1. PLATFORM STATISTICS
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Mengagregasi statistik platform secara keseluruhan.
     * Mencakup user, project, application, dan aktivitas lainnya.
     */
    @Transactional(readOnly = true)
    public PlatformStatsResponse getPlatformStats() {
        PlatformStatsResponse stats = new PlatformStatsResponse();

        // ── User metrics ───────────────────────────────────────────────────
        long totalStudents   = userRepository.countByRole(Role.MAHASISWA);
        long totalAdmins     = userRepository.countByRole(Role.ADMIN);
        long verifiedStudents = userRepository.countByRoleAndIsVerified(Role.MAHASISWA, true);

        stats.setTotalUsers(totalStudents + totalAdmins);
        stats.setTotalStudents(totalStudents);
        stats.setVerifiedStudents(verifiedStudents);
        stats.setUnverifiedStudents(totalStudents - verifiedStudents);
        stats.setTotalAdmins(totalAdmins);

        // ── Project metrics ────────────────────────────────────────────────
        long totalProjects    = projectRepository.count();
        long openProjects     = projectRepository.countByStatus(ProjectStatus.OPEN);
        long closedProjects   = projectRepository.countByStatus(ProjectStatus.CLOSED);
        long completedProjects = projectRepository.countByStatus(ProjectStatus.COMPLETED);

        stats.setTotalProjects(totalProjects);
        stats.setOpenProjects(openProjects);
        stats.setClosedProjects(closedProjects);
        stats.setCompletedProjects(completedProjects);

        // ── Application metrics ────────────────────────────────────────────
        long totalApplications   = applicationRepository.count();
        long pendingApplications = applicationRepository.countByStatus(ApplicationStatus.PENDING);
        long acceptedApplications = applicationRepository.countByStatus(ApplicationStatus.ACCEPTED);
        long rejectedApplications = applicationRepository.countByStatus(ApplicationStatus.REJECTED);

        stats.setTotalApplications(totalApplications);
        stats.setPendingApplications(pendingApplications);
        stats.setAcceptedApplications(acceptedApplications);
        stats.setRejectedApplications(rejectedApplications);

        // ── Activity metrics ───────────────────────────────────────────────
        stats.setTotalContactRequests(contactRequestRepository.count());
        stats.setTotalMessages(messageRepository.count());
        stats.setTotalSkills(skillRepository.count());

        // ── Recent Users (10 terbaru) ──────────────────────────────────────
        List<User> recentUserList = userRepository.findByRoleOrderByCreatedAtDesc(Role.MAHASISWA)
                .stream()
                .limit(10)
                .collect(Collectors.toList());
        stats.setRecentUsers(mapUsersToAdminResponse(recentUserList));

        // ── Recent Projects (10 terbaru) ───────────────────────────────────
        List<Project> recentProjectList = projectRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(10)
                .collect(Collectors.toList());
        stats.setRecentProjects(mapProjectsToSummary(recentProjectList));

        return stats;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. USER MANAGEMENT & ACCOUNT VERIFICATION
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Mendapatkan semua user mahasiswa dengan data profil lengkap.
     * Mendukung filter berdasarkan status verifikasi dan kata kunci pencarian.
     *
     * @param keyword       kata kunci pencarian (email, namaLengkap, nim) - bisa null/kosong
     * @param isVerified    filter status verifikasi - null = semua
     */
    @Transactional(readOnly = true)
    public Page<AdminUserResponse> getStudents(String keyword, Boolean isVerified, int page, int size) {
        return userRepository.findStudents(Role.MAHASISWA,
                        keyword == null || keyword.isBlank() ? null : keyword.trim(), isVerified,
                        pageRequest(page, size))
                .map(this::mapUserToAdminResponse);
    }

    /**
     * Mendapatkan detail satu user (mahasiswa) by ID beserta profil lengkap.
     *
     * @param userId ID user yang diminta
     */
    @Transactional(readOnly = true)
    public AdminUserResponse getStudentById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User tidak ditemukan dengan ID: " + userId));

        if (user.getRole() != Role.MAHASISWA) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User bukan mahasiswa");
        }

        return mapUserToAdminResponse(user);
    }

    /**
     * Memverifikasi atau membatalkan verifikasi akun mahasiswa.
     * Mengirimkan notifikasi kepada mahasiswa bersangkutan.
     *
     * @param userId  ID user yang akan diubah status verifikasinya
     * @param request DTO berisi status verifikasi baru dan alasan opsional
     */
    @Transactional
    public AdminUserResponse verifyUser(Long userId, VerifyUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User tidak ditemukan dengan ID: " + userId));

        if (user.getRole() != Role.MAHASISWA) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Hanya akun mahasiswa yang dapat diverifikasi");
        }

        boolean newStatus = request.getIsVerified();
        boolean currentStatus = user.getIsVerified();

        if (newStatus == currentStatus) {
            String msg = newStatus
                    ? "Akun sudah dalam status terverifikasi"
                    : "Akun sudah dalam status tidak terverifikasi";
            throw new ResponseStatusException(HttpStatus.CONFLICT, msg);
        }

        // Update status verifikasi
        user.setIsVerified(newStatus);
        userRepository.save(user);

        // Kirim notifikasi ke mahasiswa
        Profile profile = profileRepository.findByUserId(user.getId()).orElse(null);
        String namaDisplay = (profile != null && profile.getNamaLengkap() != null)
                ? profile.getNamaLengkap()
                : user.getEmail();

        NotificationType notifType;
        String notifTitle;
        String notifMessage;

        if (newStatus) {
            notifType    = NotificationType.ACCOUNT_VERIFIED;
            notifTitle   = "Akun Terverifikasi";
            notifMessage = "Selamat " + namaDisplay + "! Akun Anda telah diverifikasi oleh admin. " +
                           "Anda sekarang dapat mengakses seluruh fitur SkillBridge.";
        } else {
            notifType    = NotificationType.ACCOUNT_UNVERIFIED;
            notifTitle   = "Verifikasi Akun Dicabut";
            String alasan = (request.getReason() != null && !request.getReason().trim().isEmpty())
                    ? " Alasan: " + request.getReason()
                    : "";
            notifMessage = "Akun Anda telah dicabut verifikasinya oleh admin." + alasan +
                           " Silakan hubungi admin untuk informasi lebih lanjut.";
        }

        Notification notification = Notification.builder()
                .user(user)
                .type(notifType)
                .title(notifTitle)
                .message(notifMessage)
                .referenceType("USER")
                .referenceId(user.getId())
                .build();
        notificationRepository.save(notification);

        return mapUserToAdminResponse(user);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. CONTENT MODERATION
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Mendapatkan semua proyek untuk keperluan moderasi admin.
     * Mendukung filter keyword (judul/deskripsi).
     *
     * @param keyword kata kunci pencarian - bisa null/kosong
     */
    @Transactional(readOnly = true)
    public Page<PlatformStatsResponse.ProjectSummary> getAllProjectsForModeration(String keyword, int page, int size) {
        return projectRepository.findActiveForModeration(
                        keyword == null || keyword.isBlank() ? null : keyword.trim(), pageRequest(page, size))
                .map(this::buildProjectSummary);
    }

    /**
     * Menghapus proyek yang melanggar kebijakan (moderasi konten).
     * Mengirimkan notifikasi kepada pemilik proyek.
     *
     * @param projectId ID proyek yang akan dihapus
     * @param request   DTO berisi alasan penghapusan
     */
    @Transactional
    public void moderateProject(Long projectId, ModerateProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proyek tidak ditemukan dengan ID: " + projectId));

        User owner = project.getCreatedBy();
        String projectTitle = project.getTitle();

        // Soft delete agar riwayat moderasi tetap dapat diaudit.
        project.setDeletedAt(java.time.LocalDateTime.now());
        project.setStatus(ProjectStatus.CLOSED);
        projectRepository.save(project);

        // Kirim notifikasi ke pemilik proyek
        Notification notification = Notification.builder()
                .user(owner)
                .type(NotificationType.PROJECT_MODERATED)
                .title("Proyek Dihapus oleh Admin")
                .message("Proyek \"" + projectTitle + "\" telah dihapus oleh admin. " +
                         "Alasan: " + request.getReason() + ". " +
                         "Jika Anda merasa ini adalah kesalahan, silakan hubungi admin.")
                .referenceType("PROJECT")
                .referenceId(projectId)
                .build();
        notificationRepository.save(notification);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // HELPER: Mapping Methods
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Map daftar User ke daftar AdminUserResponse dengan data profil.
     */
    private List<AdminUserResponse> mapUsersToAdminResponse(List<User> users) {
        if (users.isEmpty()) return List.of();

        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());

        // Ambil semua profil sekaligus untuk menghindari N+1 query
        Map<Long, Profile> profileMap = profileRepository.findAll()
                .stream()
                .filter(p -> userIds.contains(p.getUser().getId()))
                .collect(Collectors.toMap(p -> p.getUser().getId(), p -> p));

        return users.stream()
                .map(u -> buildAdminUserResponse(u, profileMap.get(u.getId())))
                .collect(Collectors.toList());
    }

    private PageRequest pageRequest(int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page harus >= 0 dan size antara 1 hingga 100");
        }
        return PageRequest.of(page, size);
    }

    /**
     * Map satu User ke AdminUserResponse dengan data profil.
     */
    private AdminUserResponse mapUserToAdminResponse(User user) {
        Profile profile = profileRepository.findByUserId(user.getId()).orElse(null);
        return buildAdminUserResponse(user, profile);
    }

    private AdminUserResponse buildAdminUserResponse(User user, Profile profile) {
        AdminUserResponse resp = new AdminUserResponse();
        resp.setId(user.getId());
        resp.setEmail(user.getEmail());
        resp.setRole(user.getRole());
        resp.setVerified(user.getIsVerified());
        resp.setCreatedAt(user.getCreatedAt());
        resp.setUpdatedAt(user.getUpdatedAt());

        if (profile != null) {
            resp.setNamaLengkap(profile.getNamaLengkap());
            resp.setNim(profile.getNim());
            resp.setProdi(profile.getProdi());
            resp.setAngkatan(profile.getAngkatan());
            resp.setFotoUrl(profile.getFotoUrl());
        }

        return resp;
    }

    /**
     * Map daftar Project ke ProjectSummary ringkas untuk admin.
     */
    private List<PlatformStatsResponse.ProjectSummary> mapProjectsToSummary(List<Project> projects) {
        return projects.stream().map(p -> {
            User owner = p.getCreatedBy();
            Profile ownerProfile = profileRepository.findByUserId(owner.getId()).orElse(null);
            String ownerNama = (ownerProfile != null && ownerProfile.getNamaLengkap() != null)
                    ? ownerProfile.getNamaLengkap()
                    : owner.getEmail();

            return new PlatformStatsResponse.ProjectSummary(
                    p.getId(),
                    p.getTitle(),
                    p.getStatus().name(),
                    p.getCategory().name(),
                    owner.getEmail(),
                    ownerNama
            );
        }).collect(Collectors.toList());
    }

    private PlatformStatsResponse.ProjectSummary buildProjectSummary(Project p) {
        User owner = p.getCreatedBy();
        Profile ownerProfile = profileRepository.findByUserId(owner.getId()).orElse(null);
        String ownerNama = (ownerProfile != null && ownerProfile.getNamaLengkap() != null)
                ? ownerProfile.getNamaLengkap()
                : owner.getEmail();

        return new PlatformStatsResponse.ProjectSummary(
                p.getId(),
                p.getTitle(),
                p.getStatus().name(),
                p.getCategory().name(),
                owner.getEmail(),
                ownerNama
        );
    }
}
