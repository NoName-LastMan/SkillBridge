package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {

    @Size(max = 150, message = "Nama lengkap maksimal 150 karakter")
    private String namaLengkap;

    @Size(max = 20, message = "NIM maksimal 20 karakter")
    private String nim;

    @Size(max = 100, message = "Prodi maksimal 100 karakter")
    private String prodi;

    @Size(max = 50, message = "Angkatan maksimal 50 karakter")
    private String angkatan;

    private String bio;

    private String fotoUrl;

    @Size(max = 20, message = "Nomor WhatsApp maksimal 20 karakter")
    private String whatsapp;

    @Size(max = 100, message = "Username Instagram maksimal 100 karakter")
    private String instagram;

    @Size(max = 150, message = "URL LinkedIn maksimal 150 karakter")
    private String linkedin;
}
