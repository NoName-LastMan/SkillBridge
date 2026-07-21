package BackEnd.SkillBridge.dto.response;

import BackEnd.SkillBridge.entity.Role;

import java.time.LocalDateTime;

/**
 * Response DTO untuk data user yang ditampilkan di Admin Dashboard.
 * Menggabungkan data dari entitas User dan Profile.
 */
public class AdminUserResponse {

    private Long id;
    private String email;
    private Role role;
    private boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Data dari Profile
    private String namaLengkap;
    private String nim;
    private String prodi;
    private String angkatan;
    private String fotoUrl;

    // ── Constructors ───────────────────────────────────────────────────────
    public AdminUserResponse() {}

    public AdminUserResponse(Long id, String email, Role role, boolean isVerified,
                             LocalDateTime createdAt, LocalDateTime updatedAt,
                             String namaLengkap, String nim, String prodi,
                             String angkatan, String fotoUrl) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.namaLengkap = namaLengkap;
        this.nim = nim;
        this.prodi = prodi;
        this.angkatan = angkatan;
        this.fotoUrl = fotoUrl;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public Long getId()                  { return id; }
    public String getEmail()             { return email; }
    public Role getRole()                { return role; }
    public boolean isVerified()          { return isVerified; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }
    public String getNamaLengkap()       { return namaLengkap; }
    public String getNim()               { return nim; }
    public String getProdi()             { return prodi; }
    public String getAngkatan()          { return angkatan; }
    public String getFotoUrl()           { return fotoUrl; }

    public void setId(Long id)                       { this.id = id; }
    public void setEmail(String email)               { this.email = email; }
    public void setRole(Role role)                   { this.role = role; }
    public void setVerified(boolean verified)        { isVerified = verified; }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }
    public void setNamaLengkap(String namaLengkap)   { this.namaLengkap = namaLengkap; }
    public void setNim(String nim)                   { this.nim = nim; }
    public void setProdi(String prodi)               { this.prodi = prodi; }
    public void setAngkatan(String angkatan)         { this.angkatan = angkatan; }
    public void setFotoUrl(String fotoUrl)           { this.fotoUrl = fotoUrl; }
}
