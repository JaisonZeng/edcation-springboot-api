package top.alexjtech.educationapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.alexjtech.educationapi.common.ApiResponse;
import top.alexjtech.educationapi.entity.User;
import top.alexjtech.educationapi.mapper.UserMapper;

import java.util.List;

/**
 * 数据库测试控制器
 */
@Tag(name = "数据库测试接口", description = "用于测试数据库连接和基础CRUD操作")
@RestController
@RequestMapping("/test/db")
@RequiredArgsConstructor
public class DatabaseTestController {

    private final UserMapper userMapper;

    @Operation(summary = "查询所有用户", description = "测试数据库查询功能")
    @GetMapping("/users")
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userMapper.selectList(null);
        return ApiResponse.success("查询成功", users);
    }

    @Operation(summary = "根据ID查询用户", description = "测试单个用户查询")
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return ApiResponse.success("查询成功", user);
        } else {
            return ApiResponse.notFound("用户不存在");
        }
    }

    @Operation(summary = "根据OpenID查询用户", description = "测试自定义查询方法")
    @GetMapping("/users/openid/{openId}")
    public ApiResponse<User> getUserByOpenId(@PathVariable String openId) {
        User user = userMapper.selectByWechatOpenId(openId);
        if (user != null) {
            return ApiResponse.success("查询成功", user);
        } else {
            return ApiResponse.notFound("用户不存在");
        }
    }

    @Operation(summary = "创建测试用户", description = "测试数据库插入功能")
    @PostMapping("/users")
    public ApiResponse<User> createTestUser() {
        User user = new User();
        user.setWechatOpenId("test_" + System.currentTimeMillis());
        user.setNickname("测试用户_" + System.currentTimeMillis());
        user.setGender(1);
        user.setStatus(0);

        int result = userMapper.insert(user);
        if (result > 0) {
            return ApiResponse.success("创建成功", user);
        } else {
            return ApiResponse.error("创建失败");
        }
    }
}