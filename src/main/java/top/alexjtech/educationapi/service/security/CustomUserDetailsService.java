package top.alexjtech.educationapi.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.alexjtech.educationapi.entity.CustomUserDetails;
import top.alexjtech.educationapi.entity.User;
import top.alexjtech.educationapi.mapper.UserMapper;

import java.util.Collections;
import java.util.List;

/**
 * 自定义UserDetailsService实现
 * 用于从数据库加载用户信息
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 创建权限列表（这里简单处理，实际应该从数据库加载用户角色和权限）
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        // 返回自定义UserDetails对象，包含用户ID
        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getId(), // 传递用户ID
                authorities
        );
    }
}