package top.alexjtech.educationapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户信息更新DTO
 */
@Data
@Schema(description = "用户信息更新请求")
public class UserUpdateDTO {
    
    @Schema(description = "用户姓名")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;
    
    @Schema(description = "头像URL")
    private String avatar;
    
    @Schema(description = "用户昵称")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
    
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}