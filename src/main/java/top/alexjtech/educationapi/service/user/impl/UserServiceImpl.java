package top.alexjtech.educationapi.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.alexjtech.educationapi.dto.WechatLoginDTO;
import top.alexjtech.educationapi.dto.auth.LoginDTO;
import top.alexjtech.educationapi.entity.User;
import top.alexjtech.educationapi.mapper.UserMapper;
import top.alexjtech.educationapi.service.user.UserService;
import top.alexjtech.educationapi.util.security.JwtUtil;
import top.alexjtech.educationapi.vo.auth.TokenVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public TokenVO login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + loginDTO.getUsername());
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() != 0) {
            throw new BadCredentialsException("账号已被禁用");
        }

        // 检查用户类型匹配
        if (loginDTO.getUserType() != null && !loginDTO.getUserType().equals(user.getUserType())) {
            throw new BadCredentialsException("用户身份不匹配，无法以该身份登录");
        }

        // 根据用户类型设置权限
        String role = getRoleByUserType(user.getUserType());
        
        // 创建UserDetails对象
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
        );

        // 生成JWT令牌
        String token = jwtUtil.generateToken(userDetails);

        // 更新最后登录信息
        updateLastLoginInfo(user.getId(), loginDTO.getLoginIp());

        // 构建并返回令牌信息
        return TokenVO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(3600L) // 令牌有效期，单位秒
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatarUrl())
                .userType(user.getUserType())
                .build();
    }

    /**
     * 根据用户类型获取对应的角色
     */
    private String getRoleByUserType(Integer userType) {
        if (userType == null) {
            return "ROLE_USER";
        }
        switch (userType) {
            case 1:
                return "ROLE_STUDENT";
            case 2:
                return "ROLE_TEACHER";
            case 3:
                return "ROLE_ADMIN";
            default:
                return "ROLE_USER";
        }
    }

    @Override
    public TokenVO wechatLogin(WechatLoginDTO wechatLoginDTO) {
        // 这里应该调用微信API验证code并获取openId
        // 简化处理：假设已经通过code获取到了openId
        String openId = getOpenIdFromWechatCode(wechatLoginDTO.getCode());
        if (openId == null) {
            throw new BadCredentialsException("微信授权码无效");
        }

        // 根据微信OpenID查询用户
        User user = userMapper.selectByWechatOpenId(openId);
        if (user == null) {
            throw new UsernameNotFoundException("微信用户未绑定系统账号");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() != 0) {
            throw new BadCredentialsException("账号已被禁用");
        }

        // 检查用户类型匹配
        if (wechatLoginDTO.getUserType() != null && !wechatLoginDTO.getUserType().equals(user.getUserType())) {
            throw new BadCredentialsException("用户身份不匹配，无法以该身份登录");
        }

        // 根据用户类型设置权限
        String role = getRoleByUserType(user.getUserType());

        // 创建UserDetails对象
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername() != null ? user.getUsername() : user.getWechatOpenId(),
                user.getPassword() != null ? user.getPassword() : "",
                true,
                true,
                true,
                true,
                java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
        );

        // 生成JWT令牌
        String token = jwtUtil.generateToken(userDetails);

        // 更新最后登录信息
        updateLastLoginInfo(user.getId(), null); // 微信登录不记录IP

        // 构建并返回令牌信息
        return TokenVO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatarUrl())
                .userType(user.getUserType())
                .build();
    }

    /**
     * 模拟从微信code获取openId
     * 实际项目中应该调用微信官方API
     */
    private String getOpenIdFromWechatCode(String code) {
        // TODO: 实际项目中需要调用微信API
        // 这里简化处理，返回一个模拟的openId
        return "mock_openid_" + code;
    }

    @Override
    public void updateLastLoginInfo(Long userId, String loginIp) {
        userMapper.updateLastLoginInfo(userId, LocalDateTime.now(), loginIp);
    }
}