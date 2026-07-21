package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.LoginRequest;
import BackEnd.SkillBridge.dto.request.RegisterRequest;
import BackEnd.SkillBridge.dto.response.JwtResponse;
import BackEnd.SkillBridge.entity.Profile;
import BackEnd.SkillBridge.entity.User;
import BackEnd.SkillBridge.repository.ProfileRepository;
import BackEnd.SkillBridge.repository.UserRepository;
import BackEnd.SkillBridge.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Login endpoint.
     * POST /api/auth/login
     * Body: { "email": "...", "password": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getRole().name()
        ));
    }

    /**
     * Register endpoint.
     * POST /api/auth/register
     * Body: { "email": "...", "password": "...", "role": "MAHASISWA" }
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Error: Email sudah terdaftar!"));
        }

        // Create new user account
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .isVerified(true)
                .build();

        User savedUser = userRepository.save(user);

        // Auto-create profile kosong untuk UX yang lebih baik
        Profile profile = Profile.builder()
                .user(savedUser)
                .build();
        profileRepository.save(profile);

        return ResponseEntity.ok(Map.of("message", "User berhasil didaftarkan!"));
    }
}
