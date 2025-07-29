# Education API

教育平台后端API项目

## 技术栈

- Java 17
- Spring Boot 3.x
- Spring Security (JWT认证)
- MyBatis
- MySQL
- Maven

## 项目功能

- 用户注册与登录
- JWT令牌认证
- 用户头像上传与管理
- 课程管理
- 学习进度跟踪

## 运行方式

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 本地运行

1. 克隆项目到本地
   ```
   git clone <repository-url>
   ```

2. 创建数据库并执行SQL脚本
   ```
   # 创建数据库
   CREATE DATABASE education_platform;
   
   # 执行SQL脚本 (位于sql目录下)
   source sql/update_user_type_20241223.sql
   ```

3. 修改配置文件
   
   修改 `src/main/resources/application.yml` 文件中的数据库连接信息：
   
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/education_platform?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
       username: your_username
       password: your_password
   ```

4. 启动项目
   ```
   ./mvnw spring-boot:run
   ```

5. 访问API文档
   
   项目启动后，可以通过以下地址访问API文档：
   - Swagger UI: http://localhost:8080/api/swagger-ui.html
   - API文档: http://localhost:8080/api/api-docs

## 目录结构

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── top/alexjtech/educationapi/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── entity/
│   │   │       ├── mapper/
│   │   │       ├── service/
│   │   │       └── util/
│   │   └── resources/
│   └── test/
├── sql/
├── uploads/
└── problemsSolve/
```

## 配置说明

### 应用配置

在 `application.yml` 中可以配置以下重要参数：

- 服务器端口: `server.port`
- 上下文路径: `server.servlet.context-path`
- 数据库连接: `spring.datasource`
- JWT密钥: `jwt.secret`
- 文件上传路径: `app.upload.base-url`

### 静态资源配置

头像上传文件默认存储在项目根目录的 `uploads/avatar/` 文件夹中，可通过以下URL访问：

```
http://localhost:8080/api/avatar/{filename}
```

## 问题解决

项目中遇到的问题及其解决方案记录在 `problemsSolve/` 目录中，包括：

- 头像上传认证问题的排查与解决
- 其他常见问题的解决方案

## API接口

### 认证接口

- POST `/api/auth/register` - 用户注册
- POST `/api/auth/login` - 用户登录

### 用户接口

- POST `/api/user/avatar` - 上传用户头像 (需要认证)
- GET `/api/user/info` - 获取用户信息 (需要认证)

### 课程接口

- GET `/api/courses` - 获取课程列表 (需要认证)
- GET `/api/courses/{id}` - 获取课程详情 (需要认证)

## 安全配置

项目使用JWT进行接口认证，以下路径默认无需认证：

- `/api/auth/**` - 认证相关接口
- `/api/avatar/**` - 头像资源访问
- `/api/swagger-ui.html` - API文档页面
- `/api/api-docs` - API文档数据

其他所有接口都需要通过JWT令牌进行认证。