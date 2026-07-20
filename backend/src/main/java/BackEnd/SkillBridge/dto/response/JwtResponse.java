package BackEnd.SkillBridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtResponse {

    private String token;
    @lombok.Builder.Default
    private String type = "Bearer";
    private Long id;
    private String email;
    private String role;

    public JwtResponse(String token, Long id, String email, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
