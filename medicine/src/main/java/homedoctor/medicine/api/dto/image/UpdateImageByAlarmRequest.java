package homedoctor.medicine.api.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data @Getter
@Builder
public class UpdateImageByAlarmRequest {

    private List<String> image;

    public UpdateImageByAlarmRequest() {
    }

    public UpdateImageByAlarmRequest(List<String> image) {
        this.image = image;
    }
}
