package BackEnd.SkillBridge.controller;

import BackEnd.SkillBridge.dto.request.LoginRequest;
import BackEnd.SkillBridge.dto.request.RegisterRequest;
import BackEnd.SkillBridge.dto.response.JwtResponse;
import BackEnd.SkillBridge.dto.response.MessageResponse;
import BackEnd.SkillBridge.entity.User;
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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

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
                    .body(new MessageResponse("Error: Email sudah terdaftar!"));
        }

        // Create new user account
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .isVerified(true) // Set true by default; change to false if email verification needed
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User berhasil didaftarkan!"));
    }
}
