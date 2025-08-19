package com.aseubel.weave.pojo.entity.user;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * 签到统计实体
 * @author Aseubel
 * @date 2025/6/29
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "check_in_stats")
public class CheckInStats extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ToString.Exclude
    private User user;

    @Builder.Default
    @Column(name = "total_check_days")
    private Integer totalCheckDays = 0; // 总签到天数

    @Builder.Default
    @Column(name = "current_consecutive_days")
    private Integer currentConsecutiveDays = 0; // 当前连续签到天数

    @Builder.Default
    @Column(name = "max_consecutive_days")
    private Integer maxConsecutiveDays = 0; // 最大连续签到天数

    @Column(name = "last_check_date")
    private LocalDate lastCheckDate; // 最后签到日期

    @Column(name = "first_check_date")
    private LocalDate firstCheckDate; // 首次签到日期

    @Builder.Default
    @Column(name = "total_points_earned")
    private Long totalPointsEarned = 0L; // 签到总获得积分

    @Builder.Default
    @Column(name = "this_month_check_days")
    private Integer thisMonthCheckDays = 0; // 本月签到天数

    @Builder.Default
    @Column(name = "this_year_check_days")
    private Integer thisYearCheckDays = 0; // 本年签到天数

    @Builder.Default
    @Column(name = "make_up_count")
    private Integer makeUpCount = 0; // 补签次数

    @Builder.Default
    @Column(name = "available_make_up")
    private Integer availableMakeUp = 3; // 可用补签次数
}