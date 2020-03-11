package homedoctor.medicine.api.dto.user;

import lombok.Data;

@Data
public class LogInResponse {

    Long userId;

    String token;

    public LogInResponse(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
