package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    private String namaLengkap;

    @Size(max = 20)
    private String nim;

    @Size(max = 100)
    private String prodi;

    @Size(max = 10)
    private String angkatan;

    private String bio;
    private String fotoUrl;
    private String whatsapp;
    private String instagram;
    private String linkedin;

    public UpdateProfileRequest() {}

    public String getNamaLengkap() { return namaLengkap; }
    public String getNim()         { return nim; }
    public String getProdi()       { return prodi; }
    public String getAngkatan()    { return angkatan; }
    public String getBio()         { return bio; }
    public String getFotoUrl()     { return fotoUrl; }
    public String getWhatsapp()    { return whatsapp; }
    public String getInstagram()   { return instagram; }
    public String getLinkedin()    { return linkedin; }

    public void setNamaLengkap(String v)  { this.namaLengkap = v; }
    public void setNim(String v)          { this.nim = v; }
    public void setProdi(String v)        { this.prodi = v; }
    public void setAngkatan(String v)     { this.angkatan = v; }
    public void setBio(String v)          { this.bio = v; }
    public void setFotoUrl(String v)      { this.fotoUrl = v; }
    public void setWhatsapp(String v)     { this.whatsapp = v; }
    public void setInstagram(String v)    { this.instagram = v; }
    public void setLinkedin(String v)     { this.linkedin = v; }
}
