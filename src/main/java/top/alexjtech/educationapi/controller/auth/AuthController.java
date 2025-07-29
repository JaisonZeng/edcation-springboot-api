package top.alexjtech.educationapi.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alexjtech.educationapi.common.ApiResponse;
import top.alexjtech.educationapi.dto.auth.LoginDTO;
import top.alexjtech.educationapi.dto.WechatLoginDTO;
import top.alexjtech.educationapi.service.user.UserService;
import top.alexjtech.educationapi.vo.auth.TokenVO;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户登录、注册、注销等认证相关接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录并获取JWT令牌")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenVO>> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        // 获取客户端IP
        String ipAddress = getClientIp(request);
        loginDTO.setLoginIp(ipAddress);
        
        // 调用用户服务进行登录
        TokenVO tokenVO = userService.login(loginDTO);
        
        return ResponseEntity.ok(ApiResponse.success(tokenVO));
    }

    /**
     * 微信小程序登录
     */
    @Operation(summary = "微信小程序登录", description = "微信小程序用户登录并获取JWT令牌")
    @PostMapping("/wechat/login")
    public ResponseEntity<ApiResponse<TokenVO>> wechatLogin(@Valid @RequestBody WechatLoginDTO wechatLoginDTO) {
        // 调用用户服务进行微信登录
        TokenVO tokenVO = userService.wechatLogin(wechatLoginDTO);
        
        return ResponseEntity.ok(ApiResponse.success(tokenVO));
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，取第一个IP地址
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}