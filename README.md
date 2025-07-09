# Weave 项目

基于Spring Boot 3.5.3的用户认证与权限管理系统，实现了多种登录方式、双Token机制和AOP权限校验。

## 功能特性

### 1. Spring Data Redis集成
- 配置了RedisTemplate，支持字符串键和JSON值序列化
- 用于存储JWT Token、验证码等临时数据
- 提供Redis连接测试接口

### 2. 多种登录方式
- **用户名/密码登录**：传统的用户名密码认证
- **手机号登录**：通过手机号和短信验证码登录
- **微信登录**：集成微信OAuth2.0授权登录
- **QQ登录**：集成QQ OAuth2.0授权登录

### 3. 用户注册功能
- 支持用户名、密码、手机号注册
- 短信验证码验证
- 密码强度校验
- 用户名和手机号唯一性检查

### 4. 双Token机制
- **Access Token**：用于API访问，较短过期时间（2小时）
- **Refresh Token**：用于刷新Access Token，较长过期时间（7天）
- 支持Token刷新和安全注销

### 5. AOP权限校验
- **@RequireLogin**：登录校验注解
- **@RequirePermission**：角色校验注解，支持角色验证
- 支持AND/OR逻辑操作符组合多个角色条件
- ThreadLocal用户上下文管理

### 6. 安全配置
- Spring Security集成
- CORS跨域配置
- BCrypt密码加密
- JWT Token安全处理

## 技术栈

- **框架**：Spring Boot 3.5.3
- **数据库**：MySQL 8.0 + Spring Data JPA
- **缓存**：Redis + Spring Data Redis
- **安全**：Spring Security + JWT
- **工具**：Lombok、Hutool
- **构建**：Maven

## 项目结构

```
src/main/java/com/aseubel/weave/
├── WeaveApplication.java              # 启动类
├── common/
│   ├── annotation/                    # 自定义注解
│   │   ├── RequireLogin.java         # 登录校验注解
│   │   └── RequirePermission.java    # 权限校验注解
│   ├── aspect/
│   │   └── AuthAspect.java           # 权限校验切面
│   └── exception/                     # 异常处理
│       ├── AuthenticationException.java
│       ├── AuthorizationException.java
│       ├── BusinessException.java
│       └── GlobalExceptionHandler.java
├── config/                            # 配置类
│   ├── JpaConfig.java
│   ├── RedisConfig.java
│   ├── SecurityConfig.java
│   └── WebConfig.java
├── controller/                        # 控制器
│   ├── AuthController.java           # 认证相关接口
│   └── TestController.java           # 测试接口
├── pojo/
│   ├── dto/                          # 数据传输对象
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── MobileLoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── ThirdPartyLoginRequest.java
│   └── entity/                       # 实体类
│       └── User.java
├── repository/
│   └── UserRepository.java           # 数据访问层
├── service/
│   ├── UserService.java              # 用户服务接口
│   └── impl/
│       └── UserServiceImpl.java      # 用户服务实现
└── util/
    └── JwtUtil.java                   # JWT工具类
```

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/weave
    username: root
    password: root
```

### Redis配置
```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: 
      database: 0
```

### JWT配置
```yaml
jwt:
  secret: your-secret-key-here-make-it-long-enough-for-security
  access-token-expiration: 7200000    # 2小时
  refresh-token-expiration: 604800000 # 7天
```

### 第三方登录配置
```yaml
third-party:
  wechat:
    app-id: your-wechat-app-id
    app-secret: your-wechat-app-secret
    redirect-uri: http://localhost:8080/api/auth/wechat/callback
  qq:
    app-id: your-qq-app-id
    app-key: your-qq-app-key
    redirect-uri: http://localhost:8080/api/auth/qq/callback
```

## API接口

### 认证接口

#### 用户名密码登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "loginId": "username_or_mobile",
  "password": "password",
  "loginType": "USERNAME"
}
```

#### 手机号登录
```http
POST /api/auth/mobile-login
Content-Type: application/json

{
  "mobile": "13800138000",
  "smsCode": "123456"
}
```

#### 用户注册
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "confirmPassword": "password123",
  "nickname": "测试用户",
  "mobile": "13800138000",
  "smsCode": "123456"
}
```

#### Token刷新
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

#### 用户注销
```http
POST /api/auth/logout
Authorization: Bearer your-access-token
```

### 测试接口

#### 公开接口（无需登录）
```http
GET /api/test/public
```

#### 受保护接口（需要登录）
```http
GET /api/test/protected
Authorization: Bearer your-access-token
```

#### 管理员接口（需要ADMIN角色）
```http
GET /api/test/admin
Authorization: Bearer your-access-token
```

#### Redis连接测试
```http
GET /api/test/redis
```

## 权限注解使用

### @RequireLogin
```java
@GetMapping("/protected")
@RequireLogin
public ResponseEntity<?> protectedMethod() {
    // 需要登录才能访问
}
```

### @RequirePermission
```java
// 需要ADMIN角色
@GetMapping("/admin")
@RequireLogin
@RequirePermission(roles = {"ADMIN"})
public ResponseEntity<?> adminMethod() {
    // 管理员专用接口
}

// 需要特定角色
@GetMapping("/user-management")
@RequireLogin
@RequirePermission(roles = {"USER_MANAGER"})
public ResponseEntity<?> userManagementMethod() {
    // 用户管理角色
}

// 需要多个角色之一（OR逻辑）
@GetMapping("/moderator")
@RequireLogin
@RequirePermission(roles = {"ADMIN", "MODERATOR"}, logical = RequirePermission.LogicalOperator.OR)
public ResponseEntity<?> moderatorMethod() {
    // ADMIN或MODERATOR角色都可以访问
}
```

## 启动说明

1. **环境要求**
   - JDK 17+
   - MySQL 8.0+
   - Redis 6.0+
   - Maven 3.6+

2. **数据库准备**
   ```sql
   CREATE DATABASE weave CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **配置修改**
   - 修改`application.yml`中的数据库连接信息
   - 修改Redis连接信息
   - 配置JWT密钥
   - 配置第三方登录信息（可选）

4. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

5. **测试接口**
   - 访问 http://localhost:8080/api/test/public 测试公开接口
   - 访问 http://localhost:8080/api/test/redis 测试Redis连接

## 注意事项

1. **安全配置**
   - 生产环境请修改JWT密钥
   - 配置合适的CORS策略
   - 使用HTTPS协议

2. **第三方登录**
   - 需要在微信开放平台和QQ互联申请应用
   - 配置正确的回调地址
   - 当前实现为模拟数据，实际使用需要调用真实API

3. **短信服务**
   - 需要接入真实的短信服务提供商
   - 当前实现为模拟验证码

4. **数据库**
   - JPA会自动创建表结构
   - 生产环境建议手动管理数据库迁移

## 开发者

- **作者**：Aseubel
- **日期**：2025/1/27
- **版本**：1.0.0