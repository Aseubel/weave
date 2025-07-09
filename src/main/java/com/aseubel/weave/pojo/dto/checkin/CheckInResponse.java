package com.aseubel.weave.pojo.dto.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 签到响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInResponse implements Serializable {

    private Long id; // 签到记录ID
    private String message; // 消息
    private LocalDate checkDate; // 签到日期
    private LocalDateTime checkTime; // 签到时间
    private Integer pointsEarned; // 本次签到获得的积分
    private Integer consecutiveDays; // 连续签到天数
    private String location; // 签到位置
    private String remark; // 备注
    private Boolean isMakeUp; // 是否为补签

    // 签到统计信息
    private CheckInStats stats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckInStats {
        private Integer totalCheckDays; // 总签到天数
        private Integer currentConsecutiveDays; // 当前连续签到天数
        private Integer maxConsecutiveDays; // 最大连续签到天数
        private Long totalPointsEarned; // 签到总获得积分
        private Integer thisMonthCheckDays; // 本月签到天数
        private Integer thisYearCheckDays; // 本年签到天数
        private Integer availableMakeUp; // 可用补签次数
        private Boolean todayChecked; // 今天是否已签到
    }
}