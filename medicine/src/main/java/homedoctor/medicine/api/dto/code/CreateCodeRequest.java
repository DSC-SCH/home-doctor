package homedoctor.medicine.api.dto.code;

import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class CreateCodeRequest {

    private Long userId;

    public CreateCodeRequest() {

    }

    public CreateCodeRequest(Long userId) {
        this.userId = userId;
    }

    public final boolean validProperties() {
        if (userId != null) {
            return true;
        }

        return false;
    }
}
