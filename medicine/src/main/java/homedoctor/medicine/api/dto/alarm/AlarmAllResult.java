package homedoctor.medicine.api.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@Builder @RequiredArgsConstructor
public class AlarmAllResult<T> {

    private int counts;
    private T data;
}
