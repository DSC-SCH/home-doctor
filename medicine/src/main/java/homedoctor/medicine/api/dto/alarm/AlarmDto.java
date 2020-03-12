package homedoctor.medicine.api.dto.alarm;

import homedoctor.medicine.domain.AlarmStatus;
import homedoctor.medicine.domain.DateTimeEntity;
import homedoctor.medicine.domain.Label;
import homedoctor.medicine.domain.User;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor @Builder
@Getter @Setter
public class AlarmDto {

    private Long alarmId;

    private Long user;

    private String title;

    private Long label;

    private String labelTitle;

    private String color;

    private Date startDate;

    private Date endDate;

    private String times;

    private String repeats;

    private AlarmStatus alarmStatus;

    private String createdDate;

    private String lastModifiedDate;


    public static String cutDateTimeTimeValue(Date date) {
        String cutDate = date.toString().substring(0, 10);
        return cutDate;
    }
}
