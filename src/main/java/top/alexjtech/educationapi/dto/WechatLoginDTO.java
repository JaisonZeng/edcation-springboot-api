package top.alexjtech.educationapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信小程序登录请求DTO
 */
@Data
@Schema(description = "微信小程序登录请求")
public class WechatLoginDTO {

    @Schema(description = "微信授权码", required = true)
    @NotBlank(message = "微信授权码不能为空")
    private String code;

    @Schema(description = "用户信息（可选）")
    private WechatUserInfoDTO userInfo;

    @Schema(description = "用户类型（1：学生，2：老师，3：管理员）", example = "1")
    private Integer userType;
}

