package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class EnableAlarmResult<T>{
    private int counts;
    private T data;

}
