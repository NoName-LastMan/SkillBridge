package BackEnd.SkillBridge.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Format respons error standar untuk seluruh API SkillBridge.
 *
 * Contoh respons validasi gagal (400):
 * {
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "Validasi gagal",
 *   "errors": ["email: tidak boleh kosong", "password: minimal 8 karakter"],
 *   "timestamp": "2026-07-22T20:00:00",
 *   "path": "/api/auth/register"
 * }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private int status;
    private String error;
    private String message;

    /** Field-level errors dari validasi Bean Validation — null jika tidak ada. */
    private List<String> errors;

    private LocalDateTime timestamp;
    private String path;

    // ── Constructor ────────────────────────────────────────────────────────
    public ApiErrorResponse() {}

    public ApiErrorResponse(int status, String error, String message, String path) {
        this.status    = status;
        this.error     = error;
        this.message   = message;
        this.path      = path;
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(int status, String error, String message,
                            List<String> errors, String path) {
        this(status, error, message, path);
        this.errors = errors;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public int getStatus()              { return status; }
    public String getError()            { return error; }
    public String getMessage()          { return message; }
    public List<String> getErrors()     { return errors; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPath()             { return path; }

    public void setStatus(int status)              { this.status = status; }
    public void setError(String error)             { this.error = error; }
    public void setMessage(String message)         { this.message = message; }
    public void setErrors(List<String> errors)     { this.errors = errors; }
    public void setTimestamp(LocalDateTime ts)     { this.timestamp = ts; }
    public void setPath(String path)               { this.path = path; }
}
