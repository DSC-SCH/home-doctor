package homedoctor.medicine.api.dto.label;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateLabelRequest {

    private Long id;

    private String title;

    private String color;

    public final boolean validProperties() {
        if (title != null && color != null) {
            return true;
        }

        return false;
    }
}
