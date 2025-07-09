package com.aseubel.weave.pojo.dto.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 签到日历响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInCalendarResponse {

    private Integer year; // 年份
    private Integer month; // 月份
    private List<CheckInDay> checkInDays; // 签到日期列表
    private Integer monthCheckDays; // 本月签到天数
    private Integer monthTotalDays; // 本月总天数
    private CheckInResponse.CheckInStats stats; // 签到统计信息

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckInDay {
        private LocalDate date; // 日期
        private Boolean checked; // 是否已签到
        private Boolean isMakeUp; // 是否为补签
        private Integer pointsEarned; // 获得积分
        private Integer consecutiveDays; // 连续签到天数
        private Boolean canMakeUp; // 是否可以补签
    }
}