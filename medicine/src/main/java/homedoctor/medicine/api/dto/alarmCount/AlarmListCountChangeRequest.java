package homedoctor.medicine.api.dto.alarmCount;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class AlarmListCountChangeRequest {

    private List<AlarmCountRequestDto> counts;

    public AlarmListCountChangeRequest() {
    }

    public AlarmListCountChangeRequest(List<AlarmCountRequestDto> counts) {
        this.counts = counts;
    }
}
