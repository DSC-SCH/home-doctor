package homedoctor.medicine.api.dto.code.request;

import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
@Getter
public class CreateCodeRequest {

    private User user;

    private String code;

    private Integer life;

    public final boolean validProperties() {
        if (user != null && code != null && life != null) {
            return true;
        }

        return false;
    }
}
