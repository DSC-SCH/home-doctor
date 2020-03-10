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

    private Long id;

    private Alarm alarm;

    private Blob image;


}
