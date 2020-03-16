package homedoctor.medicine.api.dto.alarmCount;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AlarmListCountChangeRequest {

    private JsonArray alarmListJsonArray;

    public AlarmListCountChangeRequest() {
    }

    public AlarmListCountChangeRequest(JsonArray alarmListJsonArray) {
        this.alarmListJsonArray = alarmListJsonArray;
    }
}
