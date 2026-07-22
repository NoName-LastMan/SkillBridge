package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.response.CollaboratorMatchResult;
import BackEnd.SkillBridge.dto.response.MatchProjectResult;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    /**
     * GET /api/match/projects
     * Daftar proyek OPEN yang diurutkan dari paling cocok dengan skill saya.
     * Berguna untuk halaman "Cari Tim" / "Proyek yang Cocok Untukku".
     */
    @GetMapping("/projects")
    public ResponseEntity<List<MatchProjectResult>> matchProjects() {
        return ResponseEntity.ok(matchingService.matchProjectsForUser(getCurrentUser()));
    }

    /**
     * GET /api/match/projects/{id}
     * Hitung matching score untuk satu proyek spesifik.
     * Berguna di halaman detail proyek untuk menampilkan "X% cocok denganmu".
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<MatchProjectResult> matchSingleProject(@PathVariable Long id) {
        return ResponseEntity.ok(matchingService.matchProjectForUser(id, getCurrentUser()));
    }

    /**
     * GET /api/match/collaborators?projectId={id}
     * Temukan calon kolaborator yang skillnya cocok dengan kebutuhan proyek.
     * Berguna bagi ketua tim saat ingin mencari anggota secara aktif.
     */
    @GetMapping("/collaborators")
    public ResponseEntity<List<CollaboratorMatchResult>> findCollaborators(
            @RequestParam Long projectId) {
        return ResponseEntity.ok(matchingService.findCollaborators(projectId, getCurrentUser()));
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
