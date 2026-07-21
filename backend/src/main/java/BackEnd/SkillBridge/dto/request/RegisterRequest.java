package BackEnd.SkillBridge.dto.request;

import BackEnd.SkillBridge.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

    @NotNull(message = "Role tidak boleh kosong")
    private Role role;

    public RegisterRequest() {}

    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public Role getRole()       { return role; }

    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role)           { this.role = role; }
}
