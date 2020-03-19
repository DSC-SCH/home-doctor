package homedoctor.medicine.api.dto.user;

import lombok.Data;

@Data
public class LogInResponse {

    private Long userId;

    private String token;

    private String username;

    public LogInResponse(Long userId, String token, String username) {
        this.userId = userId;
        this.token = token;
        this.username = username;
    }
}
