package top.alexjtech.educationapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信用户信息DTO
 */
@Data
@Schema(description = "微信用户信息")
public class WechatUserInfoDTO {

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户头像")
    private String avatarUrl;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "语言")
    private String language;
}
