package homedoctor.medicine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultApiResponse<T> {
    private int status;
    private String responseMessage;
    private T data;

    public DefaultApiResponse(int status, String responseMessage) {
        this.status = status;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static<T> DefaultApiResponse<T> response(final int status, final String message) {
        return response(status, message, null);
    }

    public static<T> DefaultApiResponse<T> response(int status, String responseMessage, final T data) {
        return DefaultApiResponse.<T>builder()
                .data(data)
                .status(status)
                .responseMessage(responseMessage)
                .build();
    }
}
