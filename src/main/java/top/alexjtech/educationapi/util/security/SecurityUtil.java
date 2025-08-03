package top.alexjtech.educationapi.util.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import top.alexjtech.educationapi.entity.CustomUserDetails;
import top.alexjtech.educationapi.entity.User;

/**
 * 安全工具类
 * 用于处理认证和授权相关的工具方法
 */
public class SecurityUtil {

    /**
     * 获取当前登录用户ID
     * 
     * @param authentication 认证对象
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            // 从自定义UserDetails中直接获取用户ID
            return ((CustomUserDetails) principal).getUserId();
        }
        
        return null;
    }

    /**
     * 检查当前用户是否有权限操作指定用户的数据
     * 
     * @param authentication 认证对象
     * @param targetUserId 目标用户ID
     * @return 是否有权限
     */
    public static boolean hasPermission(Authentication authentication, Long targetUserId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Long currentUserId = getCurrentUserId(authentication);
        if (currentUserId == null) {
            return false;
        }
        
        // 管理员可以操作任何用户的数据
        if (hasRole(authentication, "ROLE_ADMIN")) {
            return true;
        }
        
        // 普通用户只能操作自己的数据
        return currentUserId.equals(targetUserId);
    }

    /**
     * 检查当前用户是否有指定角色
     * 
     * @param authentication 认证对象
     * @param role 角色名称
     * @return 是否有该角色
     */
    public static boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    /**
     * 获取当前用户的用户名
     * 
     * @param authentication 认证对象
     * @return 用户名，如果未登录返回null
     */
    public static String getCurrentUsername(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        return authentication.getName();
    }
}