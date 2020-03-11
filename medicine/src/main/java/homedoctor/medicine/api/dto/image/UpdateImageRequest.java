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

    private List<PrescriptionImage> images;

    private Alarm alarm;
}
