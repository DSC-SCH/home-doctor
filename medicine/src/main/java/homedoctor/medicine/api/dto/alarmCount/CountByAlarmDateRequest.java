package homedoctor.medicine.api.dto.alarmCount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
@Builder
public class CountByAlarmDateRequest {

    private Date date;

    public CountByAlarmDateRequest() {
    }

    public CountByAlarmDateRequest(Date date) {
        this.date = date;
    }
}
