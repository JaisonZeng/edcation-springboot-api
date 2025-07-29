package top.alexjtech.educationapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.alexjtech.educationapi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户（支持用户名、邮箱、手机号登录）
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据微信OpenID查询用户
     */
    User selectByWechatOpenId(@Param("openId") String openId);

    /**
     * 更新最后登录信息
     *
     * @param id 用户ID
     * @param loginTime 登录时间
     * @param loginIp 登录IP
     * @return 影响行数
     */
    int updateLastLoginInfo(@Param("id") Long id, @Param("loginTime") java.time.LocalDateTime loginTime, @Param("loginIp") String loginIp);
    
    /**
     * 更新最后登录信息（仅IP）
     *
     * @param id 用户ID
     * @param loginIp 登录IP
     * @return 影响行数
     */
    int updateLastLoginInfo(@Param("id") Long id, @Param("loginIp") String loginIp);

    /**
     * 检查用户名是否存在
     */
    int checkUsernameExists(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     */
    int checkEmailExists(@Param("email") String email);

    /**
     * 检查手机号是否存在
     */
    int checkPhoneExists(@Param("phone") String phone);
}