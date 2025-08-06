package com.aseubel.weave.controller;

import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.common.annotation.constraint.RequirePermission;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.redis.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器 - 演示权限校验功能
 * @author Aseubel
 * @date 2025/6/27
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final IRedisService redisService;

    /**
     * 公开接口，无需登录
     */
    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> publicEndpoint() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个公开接口，无需登录即可访问");
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 需要登录的接口
     */
    @GetMapping("/protected")
    @RequireLogin
    public ResponseEntity<Map<String, Object>> protectedEndpoint() {
        var currentUser = UserContext.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个受保护的接口，需要登录才能访问");
        result.put("currentUser", currentUser.getUsername());
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 需要管理员权限的接口
     */
    @GetMapping("/admin")
    @RequireLogin
    @RequirePermission(roles = {"ADMIN"})
    public ResponseEntity<Map<String, Object>> adminEndpoint() {
        var currentUser = UserContext.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个管理员接口，需要ADMIN角色才能访问");
        result.put("currentUser", currentUser.getUsername());
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 需要用户管理角色的接口
     */
    @GetMapping("/user-management")
    @RequireLogin
    @RequirePermission(roles = {"USER_MANAGER"}, logical = RequirePermission.LogicalOperator.AND)
    public ResponseEntity<Map<String, Object>> userManagementEndpoint() {
        var currentUser = UserContext.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个用户管理接口，需要USER_MANAGER角色才能访问");
        result.put("currentUser", currentUser.getUsername());
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 需要多个角色之一的接口
     */
    @GetMapping("/moderator")
    @RequireLogin
    @RequirePermission(roles = {"ADMIN", "MODERATOR"}, logical = RequirePermission.LogicalOperator.OR)
    public ResponseEntity<Map<String, Object>> moderatorEndpoint() {
        var currentUser = UserContext.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("message", "这是一个版主接口，需要ADMIN或MODERATOR角色才能访问");
        result.put("currentUser", currentUser.getUsername());
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 测试Redis连接
     */
    @GetMapping("/redis")
    public ResponseEntity<Map<String, Object>> testRedis() {
        try {
            // 测试Redis写入
            String testKey = "test:redis:" + System.currentTimeMillis();
            String testValue = "Hello Redis!";
            redisService.setValue(testKey, testValue);
            
            // 测试Redis读取
            String retrievedValue = redisService.getValue(testKey);
            
            // 清理测试数据
            redisService.remove(testKey);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Redis连接测试成功");
            result.put("testKey", testKey);
            result.put("testValue", testValue);
            result.put("retrievedValue", retrievedValue);
            result.put("success", testValue.equals(retrievedValue));
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Redis连接测试失败: " + e.getMessage());
            result.put("success", false);
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取系统信息
     */
    @GetMapping("/system-info")
    @RequireLogin
    @RequirePermission(roles = {"ADMIN"})
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        var currentUser = UserContext.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("message", "系统信息查询成功");
        result.put("currentUser", currentUser.getUsername());
        result.put("javaVersion", System.getProperty("java.version"));
        result.put("osName", System.getProperty("os.name"));
        result.put("osVersion", System.getProperty("os.version"));
        result.put("userDir", System.getProperty("user.dir"));
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }
}