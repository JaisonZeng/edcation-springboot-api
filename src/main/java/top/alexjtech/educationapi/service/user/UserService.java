package top.alexjtech.educationapi.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import top.alexjtech.educationapi.dto.WechatLoginDTO;
import top.alexjtech.educationapi.dto.auth.LoginDTO;
import top.alexjtech.educationapi.entity.User;
import top.alexjtech.educationapi.vo.auth.TokenVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据用户名查询用户
     * 支持用户名、邮箱、手机号登录
     *
     * @param username 用户名/邮箱/手机号
     * @return 用户对象
     */
    User getUserByUsername(String username);

    /**
     * 根据用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(Long id);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 用户登录
     *
     * @param loginDTO 登录参数
     * @return 令牌信息
     */
    TokenVO login(LoginDTO loginDTO);

    /**
     * 微信小程序登录
     *
     * @param wechatLoginDTO 微信登录参数
     * @return 令牌信息
     */
    TokenVO wechatLogin(WechatLoginDTO wechatLoginDTO);

    /**
     * 更新用户最后登录信息
     *
     * @param userId  用户ID
     * @param loginIp 登录IP
     */
    void updateLastLoginInfo(Long userId, String loginIp);

    /**
     * 根据ID更新用户信息
     *
     * @param user 用户对象
     * @return 是否更新成功
     */
    boolean updateById(User user);

    /**
     * 检查邮箱是否已存在
     * 
     * @param email 待检查的邮箱
     * @param excludeUserId 需要排除的用户ID，可为null
     * @return 如果邮箱已存在则返回true，否则返回false
     */
    boolean isEmailExists(String email, Long excludeUserId);

    /**
     * 检查手机号是否已存在
     * 
     * @param phone 待检查的手机号
     * @param excludeUserId 需要排除的用户ID，可为null
     * @return 如果手机号已存在则返回true，否则返回false
     */
    boolean isPhoneExists(String phone, Long excludeUserId);

    /**
     * 获取密码编码器
     * 
     * @return 密码编码器
     */
    org.springframework.security.crypto.password.PasswordEncoder getPasswordEncoder();
}