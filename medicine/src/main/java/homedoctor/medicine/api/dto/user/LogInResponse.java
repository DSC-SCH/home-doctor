package homedoctor.medicine.api.dto.user;

import lombok.Data;

@Data
public class LogInResponse {

    private Long userId;

    private String token;

    public LogInResponse(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
