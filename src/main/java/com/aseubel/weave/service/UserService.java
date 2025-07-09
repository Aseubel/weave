package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.auth.ChangePasswordRequest;
import com.aseubel.weave.pojo.dto.auth.LoginRequest;
import com.aseubel.weave.pojo.dto.auth.LoginResponse;
import com.aseubel.weave.pojo.dto.auth.MobileLoginRequest;
import com.aseubel.weave.pojo.dto.auth.RegisterRequest;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.common.ThirdPartyLoginRequest;
import com.aseubel.weave.pojo.dto.user.UserInfoResponse;
import com.aseubel.weave.pojo.dto.user.UserUpdateRequest;
import com.aseubel.weave.pojo.entity.user.User;

/**
 * 用户服务接口
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
public interface UserService {

    /**
     * 根据ID查找用户
     */
    User findById(Long id);

    /**
     * 用户名密码登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 手机验证码登录
     */
    LoginResponse mobileLogin(MobileLoginRequest request);

    /**
     * 第三方登录
     */
    LoginResponse thirdPartyLogin(ThirdPartyLoginRequest request);

    /**
     * 用户注册
     */
    LoginResponse register(RegisterRequest request);

    /**
     * 刷新token
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * 用户登出
     */
    void logout(Long userId);

    /**
     * 发送短信验证码
     */
    void sendSmsCode(String mobile);

    /**
     * 验证短信验证码
     */
    boolean verifySmsCode(String mobile, String code);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查手机号是否存在
     */
    boolean existsByMobile(String mobile);

    /**
     * 获取用户信息
     */
    UserInfoResponse getUserInfo(User user);

    /**
     * 更新用户信息
     */
    void updateUserInfo(User user, UserUpdateRequest request);

    /**
     * 修改密码
     */
    void changePassword(User user, ChangePasswordRequest request);

    /**
     * 获取用户列表（分页）
     */
    PageResponse<UserInfoResponse> getUserList(int page, int size, String keyword);

    /**
     * 切换用户状态（启用/禁用）
     */
    void toggleUserStatus(Long userId);
}