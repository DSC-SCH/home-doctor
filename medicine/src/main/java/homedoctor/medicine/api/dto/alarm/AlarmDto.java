package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class AlarmDto {

    private Long alarmId;

    private Long user;

    private String title;

    private Long label;

    private Date startDate;

    private Date endDate;

    private String times;

    private String repeats;

    private AlarmStatus alarmStatus;
}
