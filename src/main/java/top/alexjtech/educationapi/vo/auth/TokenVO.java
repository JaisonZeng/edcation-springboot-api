package top.alexjtech.educationapi.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 令牌返回VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "令牌信息")
public class TokenVO {

    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "过期时间（秒）", example = "3600")
    private Long expiresIn;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "用户类型（1：学生，2：老师，3：管理员）", example = "1")
    private Integer userType;
}