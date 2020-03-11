package homedoctor.medicine.api.dto.image;

import homedoctor.medicine.domain.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Blob;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CreateImageRequest {

    private Alarm alarm;

    private List<String> images;


    public final boolean validProperties() {
        if (alarm != null && images != null) {
            return true;
        }
        return false;
    }
}
