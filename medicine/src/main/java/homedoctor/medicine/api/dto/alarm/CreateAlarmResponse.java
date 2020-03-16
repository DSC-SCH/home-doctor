package homedoctor.medicine.api.dto.alarm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAlarmResponse {

    private Long id;

    public CreateAlarmResponse() {
    }

    public CreateAlarmResponse(Long id) {
        this.id = id;
    }
}
