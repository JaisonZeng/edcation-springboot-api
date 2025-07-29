package top.alexjtech.educationapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源未找到"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 业务错误
    BUSINESS_ERROR(1000, "业务处理失败"),
    VALIDATION_ERROR(1001, "参数验证失败"),
    DATA_NOT_FOUND(1002, "数据不存在"),
    DATA_ALREADY_EXISTS(1003, "数据已存在"),

    // 微信相关错误
    WECHAT_LOGIN_ERROR(2001, "微信登录失败"),
    WECHAT_CODE_INVALID(2002, "微信授权码无效"),
    WECHAT_USER_INFO_ERROR(2003, "获取微信用户信息失败");

    private final Integer code;
    private final String message;
}