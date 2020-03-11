package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
@Builder
@AllArgsConstructor
public class CreateAlarmRequest {

    private User user;

    private String title;

    private Label label;

    private Date startDate;

    private Date endDate;

    private String times;

    private String repeats;

    private AlarmStatus alarmStatus;

    public final boolean validProperties() {
        if (user != null && title != null && label != null &&
        startDate != null && endDate != null && times != null &&
        repeats != null && alarmStatus != null) {
            return true;
        }
        return false;
    }
}
