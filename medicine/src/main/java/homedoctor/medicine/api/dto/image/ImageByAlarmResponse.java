package homedoctor.medicine.api.dto.image;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder @Getter
public class ImageByAlarmResponse {

    private Long imageId;

    private String image;

    private Long alarm;
}
