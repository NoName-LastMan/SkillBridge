package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.CreateProjectRequest;
import BackEnd.SkillBridge.entity.ProjectCategory;
import BackEnd.SkillBridge.entity.Role;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.repository.UserRepository;
import BackEnd.SkillBridge.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProjectControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private MockMvc mockMvc;
    private User testUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testUser = userRepository.save(User.builder()
                .email("ketua.project@unimus.ac.id")
                .password("password123")
                .role(Role.MAHASISWA)
                .isVerified(true)
                .build());

        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());
        jwtToken = jwtUtils.generateJwtToken(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("GET /api/projects — Mengambil daftar proyek (paged)")
    void testGetAllProjectsPaged() throws Exception {
        mockMvc.perform(get("/api/projects?page=0&size=10")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.pageable").exists());
    }

    @Test
    @DisplayName("POST /api/projects — Membuat proyek baru")
    void testCreateProjectApi() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setTitle("Proyek Inovasi Digital");
        request.setDescription("Deskripsi proyek");
        request.setCategory(ProjectCategory.STARTUP);
        request.setMaxMembers(3);
        request.setRequiredSkills("Flutter, Java");

        mockMvc.perform(post("/api/projects")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Proyek Inovasi Digital"))
                .andExpect(jsonPath("$.owner").value(true));
    }
}
