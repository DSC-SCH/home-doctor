package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class AlarmAllResult<T> {

    private int counts;
    private T data;
}
