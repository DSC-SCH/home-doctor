package homedoctor.medicine.api.dto.image;

import homedoctor.medicine.domain.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Blob;

@Data
@Builder
@AllArgsConstructor
public class ImageDto {

    private Long imageId;

    private Long alarm;

    private String image;

    private String alarmTitle;

    private String labelColor;
}
