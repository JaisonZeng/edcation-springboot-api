package top.alexjtech.educationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alexjtech.educationapi.common.ApiResponse;
import top.alexjtech.educationapi.entity.User;
import top.alexjtech.educationapi.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "测试接口", description = "用于测试系统基础功能的接口")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserMapper userMapper;

    @Operation(summary = "Hello接口", description = "返回欢迎信息")
    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Hello World!");
        result.put("code", 200);
        result.put("data", "Hello World!");
        return result;
    }

    @Operation(summary = "健康检查", description = "检查系统运行状态")
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "education-api");
        result.put("version", "1.0.0");
        result.put("timestamp", LocalDateTime.now());
        return result;
    }

    @Operation(summary = "查询所有用户")
    @GetMapping("/users")
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userMapper.selectList(null);
        return ApiResponse.success("查询成功", users);
    }

    @Operation(summary = "根据用户名查询用户")
    @GetMapping("/users/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable String username) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            return ApiResponse.success("查询成功", user);
        } else {
            return ApiResponse.notFound("用户不存在");
        }
    }
}
