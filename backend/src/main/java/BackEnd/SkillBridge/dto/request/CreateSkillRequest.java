package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSkillRequest {

    @NotBlank(message = "Nama skill tidak boleh kosong")
    @Size(max = 100, message = "Nama skill maksimal 100 karakter")
    private String name;

    @Size(max = 100, message = "Kategori skill maksimal 100 karakter")
    private String category;
}
