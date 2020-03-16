package homedoctor.medicine.api.dto.alarmCount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
public class AlarmCountRequest {

    private Date date;

    public AlarmCountRequest() {
    }

    public AlarmCountRequest(Date date) {
        this.date = date;
    }
}
