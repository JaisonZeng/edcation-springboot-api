package top.alexjtech.educationapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
@Schema(description = "登录请求参数")
public class LoginDTO {

    @Schema(description = "用户名/邮箱/手机号", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "登录IP", hidden = true)
    private String loginIp;

    @Schema(description = "验证码", example = "1234")
    private String captcha;

    @Schema(description = "验证码ID", example = "abcd1234")
    private String captchaId;

    @Schema(description = "用户类型（1：学生，2：老师，3：管理员）", example = "1")
    private Integer userType;
}