package top.alexjtech.educationapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户注册请求DTO
 */
@Data
@Schema(description = "用户注册请求")
public class UserRegisterDTO {

    @Schema(description = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @Schema(description = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "昵称")
    private String nickname;
}
