package homedoctor.medicine.api.dto.alarmCount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class AlarmCountResponse {

    private Integer count;

    public AlarmCountResponse() {
    }

    public AlarmCountResponse(Integer count) {
        this.count = count;
    }
}
