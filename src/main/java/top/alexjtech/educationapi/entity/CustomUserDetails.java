package top.alexjtech.educationapi.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 自定义UserDetails实现类
 * 扩展Spring Security的User类，添加用户ID字段
 */
public class CustomUserDetails extends User {
    private Long userId;
    
    public CustomUserDetails(String username, String password, Long userId, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}