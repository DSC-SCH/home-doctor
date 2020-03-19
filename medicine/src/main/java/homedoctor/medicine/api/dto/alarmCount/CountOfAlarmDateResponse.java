package homedoctor.medicine.api.dto.alarmCount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter @Builder
public class CountOfAlarmDateResponse {

    private Long alarmId;

    private Integer count;

    public CountOfAlarmDateResponse() {
    }

    public CountOfAlarmDateResponse(Long alarmId, Integer count) {
        this.alarmId = alarmId;
        this.count = count;
    }
}
