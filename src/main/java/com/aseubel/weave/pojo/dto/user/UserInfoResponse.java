package com.aseubel.weave.pojo.dto.user;

import com.aseubel.weave.common.annotation.Desensitization;
import com.aseubel.weave.common.desensitize.DesensitizationTypeEnum;
import com.aseubel.weave.pojo.entity.Image;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户信息响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
public class UserInfoResponse {

    private Long id; // 用户ID
    private String username; // 用户名
    private String nickname; // 昵称
    private String bio; // 个人简介
    private LocalDate birthday; // 生日
    private String gender; // 性别
    private String city; // 城市
    private String profession; // 职业
    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    private String email; // 邮箱
    @Desensitization(type = DesensitizationTypeEnum.MOBILE)
    private String mobile; // 手机号（脱敏）
    private Image avatar; // 头像
    private Long points; // 积分
    private Integer level; // 等级
    private LocalDateTime createTime; // 注册时间
    private LocalDateTime lastLoginTime; // 最后登录时间
    private Boolean isActive; // 是否激活
    private Set<String> roles; // 角色集合
    private Set<InterestTagInfo> interestTags; // 兴趣标签

    // 签到相关信息
    private CheckInInfo checkInInfo;

    @Data
    @Builder
    public static class InterestTagInfo {
        private Long id;
        private String name;
        private String color;
    }

    @Data
    @Builder
    public static class CheckInInfo {
        private Integer totalCheckDays; // 总签到天数
        private Integer currentConsecutiveDays; // 当前连续签到天数
        private Integer maxConsecutiveDays; // 最大连续签到天数
        private LocalDate lastCheckDate; // 最后签到日期
        private Boolean todayChecked; // 今天是否已签到
        private Integer availableMakeUp; // 可用补签次数
    }

}