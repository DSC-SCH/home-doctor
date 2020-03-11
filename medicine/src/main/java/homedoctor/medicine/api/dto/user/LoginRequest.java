package homedoctor.medicine.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class LoginRequest {

    private Long userId;
}
