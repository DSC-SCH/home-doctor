package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import homedoctor.medicine.domain.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class UpdateAlarmRequest {

    private String title;

    private Label label;

    private Date startDate;

    private Date endDate;

    private String times;

    private String repeats;

    private AlarmStatus alarmStatus;

    public final boolean validProperties() {
        if (title != null && startDate != null
        && endDate != null && times != null && repeats != null && alarmStatus != null) {
            return true;
        }

        return false;
    }


}
