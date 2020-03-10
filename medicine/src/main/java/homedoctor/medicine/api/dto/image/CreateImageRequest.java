package homedoctor.medicine.api.dto.image;

import homedoctor.medicine.domain.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Blob;

@Data
@AllArgsConstructor
@Builder
public class CreateImageRequest {

    private Alarm alarm;

    private Blob image;


    public final boolean validProperties() {
        if (alarm != null && image != null) {
            return true;
        }
        return false;
    }
}
