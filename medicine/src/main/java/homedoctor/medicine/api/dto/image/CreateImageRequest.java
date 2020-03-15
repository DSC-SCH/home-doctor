package homedoctor.medicine.api.dto.image;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class CreateImageRequest {

    private List<String> image;

    public CreateImageRequest() {
    }

    public CreateImageRequest(List<String> images) {
        this.image = images;
    }
}
