package top.alexjtech.educationapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    /**
     * 序列化兼容控制
     */
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ApiResponse(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(500, "操作失败");
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message);
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message);
    }

    /**
     * 参数错误
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message);
    }

    /**
     * 未授权
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(401, message);
    }

    /**
     * 禁止访问
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message);
    }

    /**
     * 资源未找到
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message);
    }
}
