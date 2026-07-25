package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.request.ApplyToProjectRequest;
import BackEnd.SkillBridge.dto.request.CreateProjectRequest;
import BackEnd.SkillBridge.dto.response.ApplicationResponse;
import BackEnd.SkillBridge.dto.response.ProjectResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    private User ketua;
    private User pelamar1;
    private User pelamar2;

    @BeforeEach
    void setUp() {
        ketua = userRepository.save(User.builder()
                .email("ketua@unimus.ac.id")
                .password("password123")
                .role(Role.MAHASISWA)
                .isVerified(true)
                .build());

        pelamar1 = userRepository.save(User.builder()
                .email("pelamar1@unimus.ac.id")
                .password("password123")
                .role(Role.MAHASISWA)
                .isVerified(true)
                .build());

        pelamar2 = userRepository.save(User.builder()
                .email("pelamar2@unimus.ac.id")
                .password("password123")
                .role(Role.MAHASISWA)
                .isVerified(true)
                .build());
    }

    @Test
    @DisplayName("Buat proyek baru — Ketua otomatis jadi anggota tim pertama")
    void testCreateProject() {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setTitle("Proyek PKM");
        request.setDescription("Deskripsi proyek PKM");
        request.setCategory(ProjectCategory.PKM);
        request.setMaxMembers(2);
        request.setRequiredSkills("Java, Spring Boot");

        ProjectResponse response = projectService.createProject(ketua, request);

        assertNotNull(response);
        assertEquals("Proyek PKM", response.getTitle());
        assertEquals(ProjectStatus.OPEN, response.getStatus());
        assertEquals(1, response.getMemberCount());
        assertTrue(response.getIsOwner());
    }

    @Test
    @DisplayName("Terima lamaran & auto-close ketika kapasitas penuh")
    void testAcceptApplicationAndAutoClose() {
        // Buat proyek dengan maxMembers = 2 (Ketua + 1 anggota)
        CreateProjectRequest createReq = new CreateProjectRequest();
        createReq.setTitle("Proyek Web");
        createReq.setDescription("Deskripsi proyek web");
        createReq.setCategory(ProjectCategory.STARTUP);
        createReq.setMaxMembers(2);
        createReq.setRequiredSkills("React, Node.js");

        ProjectResponse project = projectService.createProject(ketua, createReq);

        // Pelamar 1 melamar
        ApplyToProjectRequest applyReq = new ApplyToProjectRequest();
        applyReq.setPositionApplied("Frontend Dev");
        applyReq.setMessage("Saya berminat");

        ApplicationResponse appResp1 = projectService.applyToProject(project.getId(), pelamar1, applyReq);
        assertEquals(ApplicationStatus.PENDING, appResp1.getStatus());

        // Ketua menerima lamaran 1 -> kapasitas tim mencapai 2 (ketua + pelamar1) -> status proyek otomatis CLOSED
        ApplicationResponse acceptedResp = projectService.acceptApplication(project.getId(), appResp1.getId(), ketua);
        assertEquals(ApplicationStatus.ACCEPTED, acceptedResp.getStatus());

        Project updatedProject = projectRepository.findById(project.getId()).orElseThrow();
        assertEquals(ProjectStatus.CLOSED, updatedProject.getStatus());

        // Pelamar 2 melamar setelah proyek CLOSED -> throws ResponseStatusException
        ApplyToProjectRequest applyReq2 = new ApplyToProjectRequest();
        applyReq2.setPositionApplied("Backend Dev");
        applyReq2.setMessage("Saya mau gabung");

        assertThrows(ResponseStatusException.class, () ->
                projectService.applyToProject(project.getId(), pelamar2, applyReq2));
    }
}
