package BackEnd.SkillBridge.service;

import BackEnd.SkillBridge.dto.request.ApplyToProjectRequest;
import BackEnd.SkillBridge.dto.request.CreateProjectRequest;
import BackEnd.SkillBridge.dto.request.UpdateProjectRequest;
import BackEnd.SkillBridge.dto.response.ApplicationResponse;
import BackEnd.SkillBridge.dto.response.ProjectResponse;
import BackEnd.SkillBridge.dto.response.TeamMemberResponse;
import BackEnd.SkillBridge.entity.*;
import BackEnd.SkillBridge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    // ─────────────────────────────────────────────────────────────────────
    // CRUD PROYEK
    // ─────────────────────────────────────────────────────────────────────

    @Transactional
    public ProjectResponse createProject(User creator, CreateProjectRequest request) {
        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .maxMembers(request.getMaxMembers())
                .requiredSkills(request.getRequiredSkills())
                .createdBy(creator)
                .status(ProjectStatus.OPEN)
                .build();

        Project saved = projectRepository.save(project);

        // Otomatis tambahkan creator sebagai anggota pertama (Ketua Tim)
        TeamMember ketuaTim = TeamMember.builder()
                .project(saved)
                .user(creator)
                .teamRole("Ketua Tim")
                .build();
        teamMemberRepository.save(ketuaTim);

        return buildProjectResponse(saved, creator.getId());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllOpenProjects(Long currentUserId) {
        return projectRepository.findByStatusOrderByCreatedAtDesc(ProjectStatus.OPEN)
                .stream()
                .map(p -> buildProjectResponse(p, currentUserId))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> searchProjects(String keyword, Long currentUserId) {
        return projectRepository.searchByKeyword(keyword, ProjectStatus.OPEN)
                .stream()
                .map(p -> buildProjectResponse(p, currentUserId))
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId, Long currentUserId) {
        Project project = findProjectOrThrow(projectId);
        return buildProjectResponse(project, currentUserId);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getMyProjects(Long userId) {
        return projectRepository.findByCreatedByIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(p -> buildProjectResponse(p, userId))
                .toList();
    }

    @Transactional
    public ProjectResponse updateProject(Long projectId, User currentUser, UpdateProjectRequest request) {
        Project project = findProjectOrThrow(projectId);
        validateOwner(project, currentUser.getId());

        if (request.getTitle() != null)          project.setTitle(request.getTitle());
        if (request.getDescription() != null)    project.setDescription(request.getDescription());
        if (request.getCategory() != null)       project.setCategory(request.getCategory());
        if (request.getStatus() != null)         project.setStatus(request.getStatus());
        if (request.getMaxMembers() != null)     project.setMaxMembers(request.getMaxMembers());
        if (request.getRequiredSkills() != null) project.setRequiredSkills(request.getRequiredSkills());

        return buildProjectResponse(projectRepository.save(project), currentUser.getId());
    }

    @Transactional
    public void deleteProject(Long projectId, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        validateOwner(project, currentUser.getId());
        projectRepository.delete(project);
    }

    // ─────────────────────────────────────────────────────────────────────
    // REKRUTMEN — LAMARAN
    // ─────────────────────────────────────────────────────────────────────

    @Transactional
    public ApplicationResponse applyToProject(Long projectId, User applicant,
                                               ApplyToProjectRequest request) {
        Project project = findProjectOrThrow(projectId);

        // Validasi: proyek harus OPEN
        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Proyek ini tidak sedang membuka rekrutmen");
        }

        // Validasi: tidak bisa melamar ke proyek sendiri
        if (project.getCreatedBy().getId().equals(applicant.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tidak dapat melamar ke proyek Anda sendiri");
        }

        // Validasi: belum melamar sebelumnya
        if (applicationRepository.existsByProjectIdAndApplicantId(projectId, applicant.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Anda sudah pernah melamar ke proyek ini");
        }

        // Validasi: belum jadi anggota
        if (teamMemberRepository.existsByProjectIdAndUserId(projectId, applicant.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Anda sudah menjadi anggota proyek ini");
        }

        Application application = Application.builder()
                .project(project)
                .applicant(applicant)
                .positionApplied(request.getPositionApplied())
                .message(request.getMessage())
                .status(ApplicationStatus.PENDING)
                .build();

        Application saved = applicationRepository.save(application);
        return buildApplicationResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getProjectApplications(Long projectId, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        validateOwner(project, currentUser.getId());

        return applicationRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(this::buildApplicationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(Long userId) {
        return applicationRepository.findByApplicantIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::buildApplicationResponse)
                .toList();
    }

    @Transactional
    public ApplicationResponse acceptApplication(Long projectId, Long applicationId, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        validateOwner(project, currentUser.getId());

        Application application = findApplicationOrThrow(applicationId, projectId);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Lamaran ini sudah diproses sebelumnya");
        }

        // Update status lamaran
        application.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.save(application);

        // Otomatis tambahkan sebagai anggota resmi tim
        if (!teamMemberRepository.existsByProjectIdAndUserId(projectId, application.getApplicant().getId())) {
            TeamMember member = TeamMember.builder()
                    .project(project)
                    .user(application.getApplicant())
                    .teamRole(application.getPositionApplied())
                    .build();
            teamMemberRepository.save(member);
        }

        return buildApplicationResponse(application);
    }

    @Transactional
    public ApplicationResponse rejectApplication(Long projectId, Long applicationId, User currentUser) {
        Project project = findProjectOrThrow(projectId);
        validateOwner(project, currentUser.getId());

        Application application = findApplicationOrThrow(applicationId, projectId);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Lamaran ini sudah diproses sebelumnya");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        return buildApplicationResponse(applicationRepository.save(application));
    }

    // ─────────────────────────────────────────────────────────────────────
    // TIM
    // ─────────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeamMembers(Long projectId) {
        findProjectOrThrow(projectId); // validasi project exists
        return teamMemberRepository.findByProjectId(projectId)
                .stream()
                .map(this::buildTeamMemberResponse)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────

    private Project findProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proyek tidak ditemukan"));
    }

    private Application findApplicationOrThrow(Long applicationId, Long projectId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lamaran tidak ditemukan"));
        if (!app.getProject().getId().equals(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Lamaran tidak termasuk dalam proyek ini");
        }
        return app;
    }

    private void validateOwner(Project project, Long userId) {
        if (!project.getCreatedBy().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Hanya ketua tim yang dapat melakukan aksi ini");
        }
    }

    private String getProfileName(Long userId) {
        return profileRepository.findByUserId(userId)
                .map(p -> p.getNamaLengkap())
                .orElse(null);
    }

    private String getProfileNim(Long userId) {
        return profileRepository.findByUserId(userId)
                .map(p -> p.getNim())
                .orElse(null);
    }

    private ProjectResponse buildProjectResponse(Project project, Long currentUserId) {
        int memberCount = teamMemberRepository.findByProjectId(project.getId()).size();
        int pendingCount = applicationRepository
                .findByProjectIdAndStatus(project.getId(), ApplicationStatus.PENDING).size();
        boolean hasApplied = applicationRepository
                .existsByProjectIdAndApplicantId(project.getId(), currentUserId);
        boolean isOwner = project.getCreatedBy().getId().equals(currentUserId);
        String createdByName = getProfileName(project.getCreatedBy().getId());

        return ProjectResponse.from(project, createdByName, memberCount,
                                     pendingCount, hasApplied, isOwner);
    }

    private ApplicationResponse buildApplicationResponse(Application app) {
        String name = getProfileName(app.getApplicant().getId());
        String nim  = getProfileNim(app.getApplicant().getId());
        return ApplicationResponse.from(app, name, nim);
    }

    private TeamMemberResponse buildTeamMemberResponse(TeamMember member) {
        String name = getProfileName(member.getUser().getId());
        String nim  = getProfileNim(member.getUser().getId());
        return TeamMemberResponse.from(member, name, nim);
    }
}
