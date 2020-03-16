package homedoctor.medicine.api.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor @Builder
public class ImageByUserResponse {

    private Long imageId;

    private Long alarm;

    private String image;

    private String alarmTitle;

    private String labelColor;

    private Date createdDate;
}
