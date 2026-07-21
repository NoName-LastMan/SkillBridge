package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSkillRequest {

    @NotBlank(message = "Nama skill tidak boleh kosong")
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String category;

    public CreateSkillRequest() {}

    public String getName()     { return name; }
    public String getCategory() { return category; }

    public void setName(String name)         { this.name = name; }
    public void setCategory(String category) { this.category = category; }
}
