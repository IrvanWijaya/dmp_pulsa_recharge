package dans_multi_pro.user_service.common;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse<T> {
    private String status;
    private String error;
    private T data;

    public ApiResponse() {}

    public ApiResponse(String status, String error, T data) {
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", null, data);
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>("error", errorMessage, null);
    }
}
