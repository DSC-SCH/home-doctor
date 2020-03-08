package homedoctor.medicine.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAllResult<T> {
    private int counts;
    private T data;
}
