package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import lombok.*;

import java.util.Date;

@Data
@Getter
@RequiredArgsConstructor
public class CreateAlarmRequest {

    private Long user;

    private String title;

    private Long label;

    private Date startDate;

    private Date endDate;

    private String times;

    private String repeats;

    private AlarmStatus alarmStatus;


    @Builder
    public CreateAlarmRequest(Long user, String title,
                              Long label, Date startDate,
                              Date endDate, String times,
                              String repeats, AlarmStatus alarmStatus) {
        this.user = user;
        this.title = title;
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.times = times;
        this.repeats = repeats;
        this.alarmStatus = alarmStatus;
    }

    public final boolean validProperties() {
        if (user != null && title != null && label != null &&
        startDate != null && endDate != null && times != null &&
        repeats != null && alarmStatus != null) {
            return true;
        }
        return false;
    }
}
