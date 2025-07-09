package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.checkin.CheckInCalendarResponse;
import com.aseubel.weave.pojo.dto.checkin.CheckInRequest;
import com.aseubel.weave.pojo.dto.checkin.CheckInResponse;
import com.aseubel.weave.pojo.entity.user.User;

/**
 * 签到服务接口
 * @author Aseubel
 * @date 2025/6/29
 */
public interface CheckInService {

    /**
     * 用户签到
     * @param user 用户
     * @param request 签到请求
     * @param ipAddress IP地址
     * @param deviceInfo 设备信息
     * @return 签到响应
     */
    CheckInResponse checkIn(User user, CheckInRequest request, String ipAddress, String deviceInfo);

    /**
     * 获取用户签到日历
     * @param user 用户
     * @param year 年份
     * @param month 月份
     * @return 签到日历
     */
    CheckInCalendarResponse getCheckInCalendar(User user, Integer year, Integer month);

    /**
     * 获取用户签到统计信息
     * @param user 用户
     * @return 签到响应（包含统计信息）
     */
    CheckInResponse.CheckInStats getCheckInStats(User user);

    /**
     * 检查用户今天是否已签到
     * @param user 用户
     * @return 是否已签到
     */
    boolean isTodayChecked(User user);

    /**
     * 计算签到积分
     * @param consecutiveDays 连续签到天数
     * @param isMakeUp 是否为补签
     * @return 积分
     */
    int calculateCheckInPoints(int consecutiveDays, boolean isMakeUp);

    /**
     * 初始化用户签到统计
     * @param user 用户
     */
    void initUserCheckInStats(User user);
}