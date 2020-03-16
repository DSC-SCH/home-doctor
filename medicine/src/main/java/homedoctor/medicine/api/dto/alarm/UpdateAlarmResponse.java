package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UpdateAlarmResponse {

    private Long id;

    public UpdateAlarmResponse() {
    }

    public UpdateAlarmResponse(Long id) {
        this.id = id;
    }
}
