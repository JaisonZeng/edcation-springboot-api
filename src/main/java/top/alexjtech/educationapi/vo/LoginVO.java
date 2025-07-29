package top.alexjtech.educationapi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录响应VO
 */
@Data
@Schema(description = "登录响应")
public class LoginVO {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "过期时间（秒）")
    private Long expiresIn;

    @Schema(description = "用户信息")
    private UserInfoVO userInfo;
}