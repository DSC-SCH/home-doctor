package homedoctor.medicine.api.dto.alarm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAlarmResponse {

    private Long alarmId;

    public CreateAlarmResponse() {
    }

    public CreateAlarmResponse(Long id) {
        this.alarmId = id;
    }
}
