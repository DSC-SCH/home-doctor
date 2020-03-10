package homedoctor.medicine.dto;

import homedoctor.medicine.domain.PrescriptionImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
@Builder
public class DefaultImageResponse {

    private int status;
    private String responseMessage;
    private PrescriptionImage image;
    private List<PrescriptionImage> imageList;
}
