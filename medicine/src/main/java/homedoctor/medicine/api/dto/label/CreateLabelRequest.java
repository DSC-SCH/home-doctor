package homedoctor.medicine.api.dto.label;

import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
@AllArgsConstructor
public class CreateLabelRequest {

    private User user;

    private String title;

    private String color;

    public final boolean validProperties() {
        if (user != null && title != null && color != null) {
            return true;
        }
        return false;
    }
}
