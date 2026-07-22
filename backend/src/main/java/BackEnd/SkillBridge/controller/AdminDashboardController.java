package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.ModerateProjectRequest;
import BackEnd.SkillBridge.dto.request.VerifyUserRequest;
import BackEnd.SkillBridge.dto.response.AdminUserResponse;
import BackEnd.SkillBridge.dto.response.PlatformStatsResponse;
import BackEnd.SkillBridge.service.AdminDashboardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller untuk Modul Admin Dashboard.
 *
 * Semua endpoint di bawah /api/admin/** secara otomatis diamankan oleh Spring Security
 * untuk hanya mengizinkan user dengan ROLE_ADMIN (didefinisikan di WebSecurityConfig).
 * Anotasi @PreAuthorize("hasRole('ADMIN')") ditambahkan sebagai lapisan pengaman ganda.
 *
 * Endpoints:
 *  GET    /api/admin/stats                  - Statistik platform
 *  GET    /api/admin/users                  - Daftar mahasiswa (filter: keyword, isVerified)
 *  GET    /api/admin/users/{id}             - Detail mahasiswa
 *  PUT    /api/admin/users/{id}/verify      - Verifikasi / unverify akun mahasiswa
 *  GET    /api/admin/projects               - Daftar proyek untuk moderasi (filter: keyword)
 *  DELETE /api/admin/projects/{id}          - Hapus proyek (moderasi konten)
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;

    // ═══════════════════════════════════════════════════════════════════════
    // 1. STATISTIK PLATFORM
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Mendapatkan statistik aktivitas platform secara keseluruhan.
     *
     * GET /api/admin/stats
     *
     * Response: {
     *   "totalUsers": 150, "totalStudents": 148, "verifiedStudents": 120,
     *   "unverifiedStudents": 28, "totalAdmins": 2,
     *   "totalProjects": 45, "openProjects": 30, ...
     *   "recentUsers": [...], "recentProjects": [...]
     * }
     */
    @GetMapping("/stats")
    public ResponseEntity<PlatformStatsResponse> getPlatformStats() {
        PlatformStatsResponse stats = adminDashboardService.getPlatformStats();
        return ResponseEntity.ok(stats);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. MANAJEMEN USER & VERIFIKASI AKUN
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Mendapatkan daftar semua mahasiswa dengan data profil.
     * Mendukung filter opsional berdasarkan kata kunci dan status verifikasi.
     *
     * GET /api/admin/users
     * Query Params (opsional):
     *   - keyword   : pencarian by email / namaLengkap / NIM
     *   - isVerified: true | false  (filter status verifikasi)
     */
    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserResponse>> getStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isVerified,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<AdminUserResponse> students = adminDashboardService.getStudents(keyword, isVerified, page, size);
        return ResponseEntity.ok(students);
    }

    /**
     * Mendapatkan detail satu mahasiswa berdasarkan ID.
     *
     * GET /api/admin/users/{id}
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserResponse> getStudentById(@PathVariable Long id) {
        AdminUserResponse student = adminDashboardService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * Memverifikasi atau membatalkan verifikasi akun mahasiswa.
     * Mengirimkan notifikasi otomatis kepada mahasiswa.
     *
     * PUT /api/admin/users/{id}/verify
     * Body: { "isVerified": true, "reason": "..." }
     *
     * Response: data user yang telah diupdate
     */
    @PutMapping("/users/{id}/verify")
    public ResponseEntity<AdminUserResponse> verifyUser(
            @PathVariable Long id,
            @Valid @RequestBody VerifyUserRequest request) {

        AdminUserResponse updated = adminDashboardService.verifyUser(id, request);
        return ResponseEntity.ok(updated);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. MODERASI KONTEN
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Mendapatkan semua proyek untuk keperluan moderasi.
     * Admin dapat melihat semua proyek (tanpa filter status).
     *
     * GET /api/admin/projects
     * Query Params (opsional):
     *   - keyword : pencarian by judul / deskripsi proyek
     */
    @GetMapping("/projects")
    public ResponseEntity<Page<PlatformStatsResponse.ProjectSummary>> getAllProjectsForModeration(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<PlatformStatsResponse.ProjectSummary> projects =
                adminDashboardService.getAllProjectsForModeration(keyword, page, size);
        return ResponseEntity.ok(projects);
    }

    /**
     * Menghapus proyek yang melanggar kebijakan platform (moderasi konten).
     * Admin wajib memberikan alasan penghapusan.
     * Pemilik proyek akan menerima notifikasi.
     *
     * DELETE /api/admin/projects/{id}
     * Body: { "reason": "Konten mengandung SARA/spam/melanggar kebijakan" }
     */
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Map<String, String>> moderateProject(
            @PathVariable Long id,
            @Valid @RequestBody ModerateProjectRequest request) {

        adminDashboardService.moderateProject(id, request);
        return ResponseEntity.ok(Map.of(
                "message", "Proyek berhasil dihapus dan pemilik telah dinotifikasi",
                "projectId", id.toString()
        ));
    }
}
