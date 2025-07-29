package top.alexjtech.educationapi.service.user;

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
}