package top.alexjtech.educationapi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类 - 支持多端登录
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    /**
     * 用户名（网页端登录用）
     */
    @TableField("username")
    private String username;

    /**
     * 密码（网页端登录用，加密存储）
     */
    @JsonIgnore
    @TableField("password")
    private String password;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户类型（1：学生，2：老师，3：管理员）
     */
    @TableField("user_type")
    private Integer userType;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 用户昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 性别（0：未知，1：男，2：女）
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 生日
     */
    @TableField("birthday")
    private java.time.LocalDate birthday;

    /**
     * 用户状态（0：正常，1：禁用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private java.time.LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * 微信OpenID（微信用户专用）
     */
    @TableField("wechat_open_id")
    private String wechatOpenId;

    /**
     * 微信UnionID（微信用户专用）
     */
    @TableField("wechat_union_id")
    private String wechatUnionId;

    /**
     * 微信会话密钥（微信用户专用）
     */
    @JsonIgnore
    @TableField("wechat_session_key")
    private String wechatSessionKey;

    /**
     * 备注信息
     */
    @TableField("remark")
    private String remark;
}