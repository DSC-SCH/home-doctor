package homedoctor.medicine.api.dto.alarmCount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
public class AlarmCountChangeRequest {

    private Date date;

    private Integer count;

    public AlarmCountChangeRequest() {
    }

    public AlarmCountChangeRequest(Date date, Integer count) {
        this.date = date;
        this.count = count;
    }

    public final boolean validProperties() {
        if (date != null && count != null) {
            return true;
        }

        return false;
    }
}
