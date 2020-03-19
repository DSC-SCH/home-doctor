package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import homedoctor.medicine.domain.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateAlarmDto {

    private String title;

    private Long label;

    private Date startDate;

    private Date endDate;

    private String times;

    private String repeats;

    private AlarmStatus alarmStatus;

    public UpdateAlarmDto() {
    }

    @Builder
    public UpdateAlarmDto(String title, Long label, Date startDate, Date endDate, String times, String repeats, AlarmStatus alarmStatus) {
        this.title = title;
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.times = times;
        this.repeats = repeats;
        this.alarmStatus = alarmStatus;
    }
}
