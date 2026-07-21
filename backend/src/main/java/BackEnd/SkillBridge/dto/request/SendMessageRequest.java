package BackEnd.SkillBridge.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SendMessageRequest {

    @NotBlank(message = "Isi pesan tidak boleh kosong")
    private String content;

    public SendMessageRequest() {}

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
