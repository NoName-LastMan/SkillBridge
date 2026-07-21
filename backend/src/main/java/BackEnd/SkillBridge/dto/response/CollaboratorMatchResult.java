package BackEnd.SkillBridge.dto.response;

import java.util.List;

/**
 * Hasil pencarian kolaborator: user lain yang skillnya cocok
 * dengan kebutuhan suatu proyek.
 */
public class CollaboratorMatchResult {

    // ── Info User ──────────────────────────────────────────────────────────
    private Long userId;
    private String email;
    private String namaLengkap;
    private String nim;
    private String prodi;
    private String angkatan;
    private String fotoUrl;

    // ── Matching ───────────────────────────────────────────────────────────
    /** Persentase skill yang cocok dengan kebutuhan proyek */
    private double matchScore;

    /** Nama skill yang dimiliki user dan dibutuhkan proyek */
    private List<String> matchedSkills;

    /** Semua skill yang dimiliki user (lengkap) */
    private List<UserSkillResponse> allSkills;

    // Apakah user ini sudah menjadi anggota tim proyek tersebut?
    private boolean alreadyMember;

    public CollaboratorMatchResult() {}

    // ── Getters ────────────────────────────────────────────────────────────
    public Long getUserId()                      { return userId; }
    public String getEmail()                     { return email; }
    public String getNamaLengkap()               { return namaLengkap; }
    public String getNim()                       { return nim; }
    public String getProdi()                     { return prodi; }
    public String getAngkatan()                  { return angkatan; }
    public String getFotoUrl()                   { return fotoUrl; }
    public double getMatchScore()                { return matchScore; }
    public List<String> getMatchedSkills()       { return matchedSkills; }
    public List<UserSkillResponse> getAllSkills() { return allSkills; }
    public boolean isAlreadyMember()             { return alreadyMember; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setUserId(Long userId)                        { this.userId = userId; }
    public void setEmail(String email)                        { this.email = email; }
    public void setNamaLengkap(String namaLengkap)            { this.namaLengkap = namaLengkap; }
    public void setNim(String nim)                            { this.nim = nim; }
    public void setProdi(String prodi)                        { this.prodi = prodi; }
    public void setAngkatan(String angkatan)                  { this.angkatan = angkatan; }
    public void setFotoUrl(String fotoUrl)                    { this.fotoUrl = fotoUrl; }
    public void setMatchScore(double matchScore)              { this.matchScore = matchScore; }
    public void setMatchedSkills(List<String> matchedSkills)  { this.matchedSkills = matchedSkills; }
    public void setAllSkills(List<UserSkillResponse> skills)  { this.allSkills = skills; }
    public void setAlreadyMember(boolean alreadyMember)       { this.alreadyMember = alreadyMember; }
}
