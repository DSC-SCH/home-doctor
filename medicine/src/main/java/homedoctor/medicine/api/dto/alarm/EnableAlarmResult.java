package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class EnableAlarmResult<T>{
    private int counts;
    private T data;

    public EnableAlarmResult() {
    }

    public EnableAlarmResult(int counts, T data) {
        this.counts = counts;
        this.data = data;
    }
}
