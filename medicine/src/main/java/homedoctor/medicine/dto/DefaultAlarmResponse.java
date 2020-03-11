package homedoctor.medicine.dto;

import homedoctor.medicine.domain.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DefaultAlarmResponse {

    private int status;
    private String responseMessage;
    private Alarm alarm;
    private List<Alarm> alarmList;
}
