package homedoctor.medicine.api.dto.image;

import homedoctor.medicine.domain.Alarm;
import homedoctor.medicine.domain.PrescriptionImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import java.util.List;

@Data
@AllArgsConstructor
@Builder @Getter
public class UpdateImageRequest {

    private List<String> images;

    private Long alarm;


    public final boolean validProperties() {
        if (images != null && alarm != null) {
            return true;
        }

        return false;
    }
}
