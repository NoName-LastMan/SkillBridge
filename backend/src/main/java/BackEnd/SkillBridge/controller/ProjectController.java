package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.ApplyToProjectRequest;
import BackEnd.SkillBridge.dto.request.CreateProjectRequest;
import BackEnd.SkillBridge.dto.request.UpdateProjectRequest;
import BackEnd.SkillBridge.dto.response.ApplicationResponse;
import BackEnd.SkillBridge.dto.response.ProjectResponse;
import BackEnd.SkillBridge.dto.response.TeamMemberResponse;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // ─────────────────────────────────────────────────────────────────────
    // CRUD PROYEK
    // ─────────────────────────────────────────────────────────────────────

    /**
     * POST /api/projects
     * Buat proyek baru. Creator otomatis jadi Ketua Tim.
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(getCurrentUser(), request));
    }

    /**
     * GET /api/projects
     * Browse semua proyek OPEN. Mendukung pencarian dengan ?q=keyword
     */
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = getCurrentUser().getId();
        if (q != null && !q.isBlank()) {
            return ResponseEntity.ok(projectService.searchProjects(q, userId, page, size));
        }
        return ResponseEntity.ok(projectService.getAllOpenProjects(userId, page, size));
    }

    /**
     * GET /api/projects/my
     * Proyek yang saya buat (sebagai ketua).
     */
    @GetMapping("/my")
    public ResponseEntity<List<ProjectResponse>> getMyProjects() {
        return ResponseEntity.ok(projectService.getMyProjects(getCurrentUser().getId()));
    }

    /**
     * GET /api/projects/{id}
     * Detail satu proyek.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id, getCurrentUser().getId()));
    }

    /**
     * PUT /api/projects/{id}
     * Update proyek — hanya ketua tim.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, getCurrentUser(), request));
    }

    /**
     * DELETE /api/projects/{id}
     * Hapus proyek — hanya ketua tim.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id, getCurrentUser());
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────────────────────────────
    // REKRUTMEN — LAMARAN
    // ─────────────────────────────────────────────────────────────────────

    /**
     * POST /api/projects/{id}/apply
     * Melamar ke proyek.
     * Body: { "positionApplied": "...", "message": "..." }
     */
    @PostMapping("/{id}/apply")
    public ResponseEntity<ApplicationResponse> applyToProject(
            @PathVariable Long id,
            @Valid @RequestBody ApplyToProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.applyToProject(id, getCurrentUser(), request));
    }

    /**
     * GET /api/projects/{id}/applications
     * Lihat semua lamaran masuk — hanya ketua tim.
     */
    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>> getApplications(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectApplications(id, getCurrentUser()));
    }

    /**
     * PUT /api/projects/{id}/applications/{appId}/accept
     * Terima lamaran → pelamar otomatis jadi anggota tim.
     */
    @PutMapping("/{id}/applications/{appId}/accept")
    public ResponseEntity<ApplicationResponse> acceptApplication(
            @PathVariable Long id,
            @PathVariable Long appId) {
        return ResponseEntity.ok(projectService.acceptApplication(id, appId, getCurrentUser()));
    }

    /**
     * PUT /api/projects/{id}/applications/{appId}/reject
     * Tolak lamaran.
     */
    @PutMapping("/{id}/applications/{appId}/reject")
    public ResponseEntity<ApplicationResponse> rejectApplication(
            @PathVariable Long id,
            @PathVariable Long appId) {
        return ResponseEntity.ok(projectService.rejectApplication(id, appId, getCurrentUser()));
    }

    // ─────────────────────────────────────────────────────────────────────
    // TIM
    // ─────────────────────────────────────────────────────────────────────

    /**
     * GET /api/projects/{id}/team
     * Lihat anggota resmi tim proyek.
     */
    @GetMapping("/{id}/team")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getTeamMembers(id));
    }

    // ─────────────────────────────────────────────────────────────────────
    // LAMARAN SAYA
    // ─────────────────────────────────────────────────────────────────────

    /**
     * GET /api/projects/applications/my
     * Lihat semua lamaran yang sudah saya kirim.
     */
    @GetMapping("/applications/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        return ResponseEntity.ok(projectService.getMyApplications(getCurrentUser().getId()));
    }

    // ─────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
