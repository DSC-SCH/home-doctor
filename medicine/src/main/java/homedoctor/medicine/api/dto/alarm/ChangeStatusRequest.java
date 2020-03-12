package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor @Getter
public class ChangeStatusRequest {

    private AlarmStatus alarmStatus;

    private Long id;
}
