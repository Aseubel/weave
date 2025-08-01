package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.auth.*;
import com.aseubel.weave.pojo.dto.common.ThirdPartyLoginRequest;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 手机验证码登录
     */
    @PostMapping("/mobile-login")
    public ApiResponse<LoginResponse> mobileLogin(@Valid @RequestBody MobileLoginRequest request) {
        try {
            LoginResponse response = userService.mobileLogin(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 第三方登录
     */
    @PostMapping("/third-party-login")
    public ApiResponse<LoginResponse> thirdPartyLogin(@Valid @RequestBody ThirdPartyLoginRequest request) {
        try {
            LoginResponse response = userService.thirdPartyLogin(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            LoginResponse response = userService.register(request);
            return ApiResponse.success("注册成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 刷新token
     */
    @PostMapping("/refresh-token")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                return ApiResponse.badRequest("刷新令牌不能为空");
            }
            LoginResponse response = userService.refreshToken(refreshToken);
            return ApiResponse.success("刷新成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @RequireLogin
    public ApiResponse<Void> logout() {
        try {
            Long userId = UserContext.getCurrentUser().getId();
            userService.logout(userId);
            return ApiResponse.success("登出成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 发送短信验证码 todo 暂时直接返回验证码便于测试，后续需要改为发送短信
     */
    @PostMapping("/send-sms-code")
    public ApiResponse<String> sendSmsCode(@RequestBody SmsRequest request) {
        try {
            String mobile = request.getMobile();

            if (mobile == null || mobile.trim().isEmpty()) {
                return ApiResponse.badRequest("手机号不能为空");
            }

            // 验证手机号格式
            if (!mobile.matches("^1[3-9]\\d{9}$")) {
                return ApiResponse.badRequest("手机号格式不正确");
            }

            String code = userService.sendSmsCode(mobile);
            return ApiResponse.success("验证码发送成功", code);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public ApiResponse<Map<String, Object>> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            Map<String, Object> data = new HashMap<>();
            data.put("exists", exists);
            return ApiResponse.success("查询成功", data);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 检查手机号是否存在
     */
    @GetMapping("/check-mobile")
    public ApiResponse<Map<String, Object>> checkMobile(@RequestParam String mobile) {
        try {
            boolean exists = userService.existsByMobile(mobile);
            Map<String, Object> data = new HashMap<>();
            data.put("exists", exists);
            return ApiResponse.success("查询成功", data);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @RequireLogin
    public ApiResponse<Map<String, Object>> getCurrentUser() {
        try {
            User currentUser = UserContext.getCurrentUser();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", currentUser.getId());
            userInfo.put("username", currentUser.getUsername());
            userInfo.put("nickname", currentUser.getNickname());
            userInfo.put("avatarUrl", currentUser.getAvatar());
            userInfo.put("mobile", currentUser.getMobile());
            userInfo.put("points", currentUser.getPoints());

            return ApiResponse.success("获取成功", userInfo);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 微信登录回调
     */
    @GetMapping("/wechat/callback")
    public ApiResponse<LoginResponse> wechatCallback(@RequestParam String code,
            @RequestParam(required = false) String state) {
        try {
            ThirdPartyLoginRequest request = new ThirdPartyLoginRequest();
            request.setCode(code);
            request.setState(state);
            request.setType(ThirdPartyLoginRequest.LoginType.WECHAT);

            LoginResponse response = userService.thirdPartyLogin(request);
            return ApiResponse.success("微信登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * QQ登录回调
     */
    @GetMapping("/qq/callback")
    public ApiResponse<LoginResponse> qqCallback(@RequestParam String code,
            @RequestParam(required = false) String state) {
        try {
            ThirdPartyLoginRequest request = new ThirdPartyLoginRequest();
            request.setCode(code);
            request.setState(state);
            request.setType(ThirdPartyLoginRequest.LoginType.QQ);

            LoginResponse response = userService.thirdPartyLogin(request);
            return ApiResponse.success("QQ登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

}