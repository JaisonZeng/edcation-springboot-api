package top.alexjtech.educationapi.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.alexjtech.educationapi.common.ApiResponse;
import top.alexjtech.educationapi.dto.user.UserUpdateDTO;
import top.alexjtech.educationapi.dto.user.PasswordChangeDTO;
import top.alexjtech.educationapi.entity.CustomUserDetails;
import top.alexjtech.educationapi.entity.User;
import top.alexjtech.educationapi.service.user.UserService;
import top.alexjtech.educationapi.util.security.SecurityUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 用户管理控制器
 * 处理用户相关的操作，如头像上传、用户信息更新等
 */
@Tag(name = "用户管理", description = "用户头像上传、信息更新等接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;
  
  // 从配置文件读取上传配置
  @Value("${app.upload.upload-dir:uploads/}")
  private String uploadDir;
  
  @Value("${app.upload.base-url:http://127.0.0.1:8080/api/}")
  private String baseUrl;
  
  @Value("${app.upload.max-size:5242880}") // 5MB
  private long maxFileSize;
  
  // 允许的图片格式
  private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
      "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
  );
  
  // 允许的文件扩展名
  private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
      ".jpg", ".jpeg", ".png", ".gif", ".webp"
  );

  /**
   * 头像上传接口
   * URL: /api/user/upload-avatar
   * Method: POST
   * Content-Type: multipart/form-data
   * Authorization: Bearer Token
   */
  @Operation(summary = "上传头像", description = "上传用户头像图片")
  @PostMapping("/upload-avatar")
  public ResponseEntity<ApiResponse<Object>> uploadAvatar(
          @RequestParam("avatar") MultipartFile avatar,
          Authentication authentication) {
      
      try {
          // 获取当前登录用户ID
          Long currentUserId = SecurityUtil.getCurrentUserId(authentication);
          if (currentUserId == null) {
              return ResponseEntity.status(401)
                      .body(ApiResponse.unauthorized("请先登录"));
          }
          
          // 验证文件
          if (avatar.isEmpty()) {
              return ResponseEntity.badRequest()
                      .body(ApiResponse.error("请选择头像文件"));
          }
          
          // 验证文件类型
          String contentType = avatar.getContentType();
          if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
              return ResponseEntity.badRequest()
                      .body(ApiResponse.error("不支持的文件格式，请上传 JPG、PNG、GIF 或 WebP 格式的图片"));
          }
          
          // 验证文件扩展名
          String originalFilename = avatar.getOriginalFilename();
          if (originalFilename == null || !hasValidExtension(originalFilename)) {
              return ResponseEntity.badRequest()
                      .body(ApiResponse.error("不支持的文件扩展名"));
          }
          
          // 验证文件大小
          if (avatar.getSize() > maxFileSize) {
              return ResponseEntity.badRequest()
                      .body(ApiResponse.error("文件大小不能超过5MB"));
          }
          
          // 创建上传目录
          Path uploadPath = Paths.get(uploadDir + "/avatars/");
          if (!Files.exists(uploadPath)) {
              Files.createDirectories(uploadPath);
          }
          
          // 生成唯一文件名
          String fileExtension = getFileExtension(originalFilename);
          String fileName = "avatar_" + currentUserId + "_" + UUID.randomUUID().toString() + fileExtension;
          
          // 保存文件
          Path filePath = uploadPath.resolve(fileName);
          Files.write(filePath, avatar.getBytes());
          
          // 构建文件URL
          String avatarUrl = baseUrl + uploadDir + "avatars/" + fileName;
          
          // 更新用户头像信息
          User user = userService.getUserById(currentUserId);
          if (user == null) {
              // 删除已上传的文件
              Files.deleteIfExists(filePath);
              return ResponseEntity.status(404)
                      .body(ApiResponse.notFound("用户不存在"));
          }
          
          // 删除旧头像文件（如果存在）
          deleteOldAvatar(user.getAvatarUrl());
          
          // 更新头像URL
          user.setAvatarUrl(avatarUrl);
          userService.updateById(user);
          
          log.info("用户 {} 上传头像成功: {}", currentUserId, fileName);
          
          // 返回结果
          return ResponseEntity.ok(ApiResponse.success("头像上传成功", 
              java.util.Map.of(
                  "avatarUrl", avatarUrl,
                  "fileName", fileName
              )));
          
      } catch (IOException e) {
          log.error("头像上传失败", e);
          return ResponseEntity.internalServerError()
                  .body(ApiResponse.error("文件上传失败，请重试"));
      } catch (Exception e) {
          log.error("头像上传异常", e);
          return ResponseEntity.internalServerError()
                  .body(ApiResponse.error("系统异常，请稍后重试"));
      }
  }

  /**
   * 更新用户信息接口
   * URL: /api/user/update
   * Method: PUT
   * Content-Type: application/json
   * Authorization: Bearer Token
   */
  @Operation(summary = "更新用户信息", description = "更新当前登录用户的信息")
  @PutMapping("/update")
  public ResponseEntity<ApiResponse<User>> updateUserInfo(
          @Valid @RequestBody UserUpdateDTO userUpdateDTO,
          Authentication authentication) {
      
      try {
          // 获取当前登录用户ID
          Long currentUserId = SecurityUtil.getCurrentUserId(authentication);
          if (currentUserId == null) {
              return ResponseEntity.status(401)
                      .body(ApiResponse.unauthorized("请先登录"));
          }
          
          // 获取用户信息
          User user = userService.getUserById(currentUserId);
          if (user == null) {
              return ResponseEntity.status(404)
                      .body(ApiResponse.notFound("用户不存在"));
          }
          
          // 更新用户信息
          boolean updated = false;
          
          if (userUpdateDTO.getName() != null && !userUpdateDTO.getName().equals(user.getRealName())) {
              user.setRealName(userUpdateDTO.getName());
              updated = true;
          }
          
          if (userUpdateDTO.getNickname() != null && !userUpdateDTO.getNickname().equals(user.getNickname())) {
              user.setNickname(userUpdateDTO.getNickname());
              updated = true;
          }
          
          if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(user.getEmail())) {
              // 检查邮箱是否已被其他用户使用
              if (userService.isEmailExists(userUpdateDTO.getEmail(), currentUserId)) {
                  return ResponseEntity.badRequest()
                          .body(ApiResponse.error("该邮箱已被其他用户使用"));
              }
              user.setEmail(userUpdateDTO.getEmail());
              updated = true;
          }
          
          if (userUpdateDTO.getPhone() != null && !userUpdateDTO.getPhone().equals(user.getPhone())) {
              // 检查手机号是否已被其他用户使用
              if (userService.isPhoneExists(userUpdateDTO.getPhone(), currentUserId)) {
                  return ResponseEntity.badRequest()
                          .body(ApiResponse.error("该手机号已被其他用户使用"));
              }
              user.setPhone(userUpdateDTO.getPhone());
              updated = true;
          }
          
          if (!updated) {
              return ResponseEntity.ok(ApiResponse.success("没有需要更新的信息", user));
          }
          
          // 保存更新
          userService.updateById(user);
          
          log.info("用户 {} 更新信息成功", currentUserId);
          
          // 返回更新后的用户信息（不包含敏感信息）
          user.setPassword(null); // 不返回密码
          return ResponseEntity.ok(ApiResponse.success("用户信息更新成功", user));
          
      } catch (Exception e) {
          log.error("更新用户信息异常", e);
          return ResponseEntity.internalServerError()
                  .body(ApiResponse.error("系统异常，请稍后重试"));
      }
  }
  
  /**
   * 获取当前用户信息
   */
  @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<User>> getUserProfile(Authentication authentication) {
      try {
          Long currentUserId = SecurityUtil.getCurrentUserId(authentication);
          if (currentUserId == null) {
              return ResponseEntity.status(401)
                      .body(ApiResponse.unauthorized("请先登录"));
          }
          
          User user = userService.getUserById(currentUserId);
          if (user == null) {
              return ResponseEntity.status(404)
                      .body(ApiResponse.notFound("用户不存在"));
          }
          
          // 不返回密码等敏感信息
          user.setPassword(null);
          
          return ResponseEntity.ok(ApiResponse.success("获取成功", user));
          
      } catch (Exception e) {
          log.error("获取用户信息异常", e);
          return ResponseEntity.internalServerError()
                  .body(ApiResponse.error("系统异常，请稍后重试"));
      }
  }
  
  /**
   * 验证文件扩展名
   */
  private boolean hasValidExtension(String filename) {
      String extension = getFileExtension(filename).toLowerCase();
      return ALLOWED_EXTENSIONS.contains(extension);
  }
  
  /**
   * 获取文件扩展名
   */
  private String getFileExtension(String filename) {
      int lastDotIndex = filename.lastIndexOf(".");
      return lastDotIndex > 0 ? filename.substring(lastDotIndex) : ".jpg";
  }
  
  /**
   * 删除旧头像文件
   */
  private void deleteOldAvatar(String oldAvatarUrl) {
      if (oldAvatarUrl != null && oldAvatarUrl.startsWith(baseUrl)) {
          try {
              String fileName = oldAvatarUrl.substring(baseUrl.length());
              Path oldFilePath = Paths.get(fileName);
              Files.deleteIfExists(oldFilePath);
              log.info("删除旧头像文件: {}", fileName);
          } catch (Exception e) {
              log.warn("删除旧头像文件失败: {}", oldAvatarUrl, e);
          }
      }
  }
  
  /**
   * 修改用户密码
   * 
   * @param passwordChangeDTO 密码修改请求数据
   * @param authentication    认证信息
   * @return 修改结果
   */
  @Operation(summary = "修改用户密码", description = "修改当前登录用户的密码")
  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse<String>> changePassword(
          @Valid @RequestBody PasswordChangeDTO passwordChangeDTO,
          Authentication authentication) {
      
      try {
          // 获取当前用户
          CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
          User currentUser = userService.getUserById(userDetails.getUserId());
          
          if (currentUser == null) {
              return ResponseEntity.status(404)
                      .body(ApiResponse.notFound("用户不存在"));
          }
          
          // 验证原密码是否正确
          if (!userService.getPasswordEncoder().matches(passwordChangeDTO.getOldPassword(), currentUser.getPassword())) {
              return ResponseEntity.badRequest()
                      .body(ApiResponse.error("原密码不正确"));
          }
          
          // 验证新密码与原密码是否相同
          if (userService.getPasswordEncoder().matches(passwordChangeDTO.getNewPassword(), currentUser.getPassword())) {
              return ResponseEntity.badRequest()
                      .body(ApiResponse.error("新密码不能与原密码相同"));
          }
          
          // 更新密码
          String encodedNewPassword = userService.getPasswordEncoder().encode(passwordChangeDTO.getNewPassword());
          currentUser.setPassword(encodedNewPassword);
          boolean success = userService.updateById(currentUser);
          
          if (success) {
              log.info("用户 {} 密码修改成功", currentUser.getId());
              return ResponseEntity.ok(ApiResponse.success("密码修改成功"));
          } else {
              log.error("用户 {} 密码修改失败", currentUser.getId());
              return ResponseEntity.status(500)
                      .body(ApiResponse.error("密码修改失败"));
          }
          
      } catch (Exception e) {
          log.error("密码修改过程中发生错误", e);
          return ResponseEntity.status(500)
                  .body(ApiResponse.error("服务器内部错误"));
      }
  }
}