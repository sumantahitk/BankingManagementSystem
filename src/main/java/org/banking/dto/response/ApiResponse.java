package org.banking.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
    private String error;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).status(200).message(message).data(data).build();
    }
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).status(status).message(message).data(data).build();
    }
    public static <T> ApiResponse<T> error(int status, String message, String error) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).status(status).message(message).error(error).build();
    }
}
