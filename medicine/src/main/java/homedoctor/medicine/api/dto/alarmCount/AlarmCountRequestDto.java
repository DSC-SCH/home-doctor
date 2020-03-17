package homedoctor.medicine.api.dto.alarmCount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data @Builder @Getter
public class AlarmCountRequestDto {

    private Long alarmId;
    private Date date;
    private Integer count;

    public AlarmCountRequestDto() {
    }

    public AlarmCountRequestDto(Long alarmId, Date date, Integer count) {
        this.alarmId = alarmId;
        this.date = date;
        this.count = count;
    }
}
