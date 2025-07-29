package top.alexjtech.educationapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "网页端登录请求")
public class WebLoginDTO {
    @Schema(description = "用户名或邮箱或手机号", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "记住我", example = "false")
    private Boolean rememberMe = false;
}
