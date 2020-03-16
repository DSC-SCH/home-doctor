package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
public class ChangeStatusRequest {

    private AlarmStatus alarmStatus;

    public ChangeStatusRequest() {
    }

    public ChangeStatusRequest(AlarmStatus alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
}
