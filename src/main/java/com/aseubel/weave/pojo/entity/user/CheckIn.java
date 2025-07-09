package com.aseubel.weave.pojo.entity.user;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 签到记录实体
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
@Table(name = "check_in", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "check_date"}))
public class CheckIn extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate; // 签到日期

    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime; // 签到时间

    @Builder.Default
    @Column(name = "points_earned")
    private Integer pointsEarned = 0; // 本次签到获得的积分

    @Builder.Default
    @Column(name = "consecutive_days")
    private Integer consecutiveDays = 1; // 连续签到天数

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // 签到IP地址

    @Column(name = "device_info", length = 200)
    private String deviceInfo; // 设备信息

    @Column(name = "location", length = 100)
    private String location; // 签到位置（可选）

    @Builder.Default
    @Column(name = "is_补签")
    private Boolean isMakeUp = false; // 是否为补签

    @Column(name = "remark", length = 500)
    private String remark; // 备注
}