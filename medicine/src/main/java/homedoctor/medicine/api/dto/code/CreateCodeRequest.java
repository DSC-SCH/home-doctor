package homedoctor.medicine.api.dto.code;

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

    private Long userId;

    private String code;

    private Integer life;

    public final boolean validProperties() {
        if (userId != null && code != null && life != null) {
            return true;
        }

        return false;
    }
}
