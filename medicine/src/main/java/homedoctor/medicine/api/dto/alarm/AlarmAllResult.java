package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AlarmAllResult<T> {

    private int counts;
    private T data;
}
