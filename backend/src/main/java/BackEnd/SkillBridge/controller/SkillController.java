package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.CreateSkillRequest;
import BackEnd.SkillBridge.dto.response.SkillResponse;
import BackEnd.SkillBridge.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SkillController {

    @Autowired
    private SkillService skillService;

    /**
     * GET /api/skills
     * List semua skill dari tabel master.
     */
    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    /**
     * GET /api/skills/search?q=java
     * Cari skill berdasarkan keyword nama.
     */
    @GetMapping("/search")
    public ResponseEntity<List<SkillResponse>> searchSkills(
            @RequestParam("q") String keyword) {
        return ResponseEntity.ok(skillService.searchSkills(keyword));
    }

    /**
     * POST /api/skills
     * Buat skill baru di master tabel. Hanya ADMIN.
     * Body: { "name": "Spring Boot", "category": "Backend" }
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SkillResponse> createSkill(
            @Valid @RequestBody CreateSkillRequest request) {
        return ResponseEntity.ok(skillService.createSkill(request));
    }
}
