package top.alexjtech.educationapi.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.alexjtech.educationapi.util.security.JwtUtil;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于拦截请求，验证JWT令牌，并设置认证信息
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 检查Authorization头是否存在且以Bearer开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                logger.error("JWT令牌验证失败: " + e.getMessage());
            }
        }

        // 如果找到了用户名且当前没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 从用户服务加载用户详情
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 验证令牌
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 设置认证信息
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
}