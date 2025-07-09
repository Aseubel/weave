package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.common.annotation.constraint.RequirePermission;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.auth.ChangePasswordRequest;
import com.aseubel.weave.pojo.dto.checkin.CheckInCalendarResponse;
import com.aseubel.weave.pojo.dto.checkin.CheckInRequest;
import com.aseubel.weave.pojo.dto.checkin.CheckInResponse;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.user.UserInfoResponse;
import com.aseubel.weave.pojo.dto.user.UserUpdateRequest;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.service.CheckInService;
import com.aseubel.weave.service.UserService;
import com.aseubel.weave.common.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 
 * @author Aseubel
 * @date 2025/6/28 下午10:13
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CheckInService checkInService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    @RequireLogin
    public ApiResponse<UserInfoResponse> getUserInfo() {
        User currentUser = UserContext.getCurrentUser();
        UserInfoResponse userInfo = userService.getUserInfo(currentUser);
        return ApiResponse.success(userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    @RequireLogin
    public ApiResponse<String> updateUserInfo(@Valid @RequestBody UserUpdateRequest request) {
        User currentUser = UserContext.getCurrentUser();
        userService.updateUserInfo(currentUser, request);
        return ApiResponse.success("用户信息更新成功");
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    @RequireLogin
    public ApiResponse<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        User currentUser = UserContext.getCurrentUser();
        userService.changePassword(currentUser, request);
        return ApiResponse.success("密码修改成功");
    }

    /**
     * 用户签到
     */
    @PostMapping("/check-in")
    @RequireLogin
    public ApiResponse<CheckInResponse> checkIn(
            @RequestBody(required = false) CheckInRequest request,
            HttpServletRequest httpRequest) {
        User currentUser = UserContext.getCurrentUser();
        if (request == null) {
            request = new CheckInRequest();
        }

        String ipAddress = IpUtil.getClientIpAddress(httpRequest);
        String deviceInfo = IpUtil.getDeviceInfo(httpRequest);

        CheckInResponse response = checkInService.checkIn(currentUser, request, ipAddress, deviceInfo);
        return ApiResponse.success(response);
    }

    /**
     * 获取签到日历
     */
    @GetMapping("/check-in/calendar")
    @RequireLogin
    public ApiResponse<CheckInCalendarResponse> getCheckInCalendar(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        User currentUser = UserContext.getCurrentUser();
        CheckInCalendarResponse calendar = checkInService.getCheckInCalendar(currentUser, year, month);
        return ApiResponse.success(calendar);
    }

    /**
     * 获取签到统计
     */
    @GetMapping("/check-in/stats")
    @RequireLogin
    public ApiResponse<CheckInResponse.CheckInStats> getCheckInStats() {
        User currentUser = UserContext.getCurrentUser();
        CheckInResponse.CheckInStats stats = checkInService.getCheckInStats(currentUser);
        return ApiResponse.success(stats);
    }

    /**
     * 检查今天是否已签到
     */
    @GetMapping("/check-in/today")
    @RequireLogin
    public ApiResponse<Boolean> isTodayChecked() {
        User currentUser = UserContext.getCurrentUser();
        boolean checked = checkInService.isTodayChecked(currentUser);
        return ApiResponse.success(checked);
    }

    /**
     * 获取用户列表（管理员功能）
     */
    @GetMapping("/list")
    @RequirePermission(roles = { "ADMIN" })
    public ApiResponse<PageResponse<UserInfoResponse>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        PageResponse<UserInfoResponse> userList = userService.getUserList(page, size, keyword);
        return ApiResponse.success(userList);
    }

    /**
     * 禁用/启用用户（管理员功能）
     */
    @PutMapping("/{userId}/status")
    @RequirePermission(roles = { "ADMIN" })
    public ApiResponse<String> toggleUserStatus(@PathVariable Long userId) {
        userService.toggleUserStatus(userId);
        return ApiResponse.success("用户状态更新成功");
    }
}
