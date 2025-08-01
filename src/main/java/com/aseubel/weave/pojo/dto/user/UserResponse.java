package com.aseubel.weave.pojo.dto.user;

import com.aseubel.weave.common.annotation.Desensitization;
import com.aseubel.weave.common.desensitize.DesensitizationTypeEnum;
import com.aseubel.weave.pojo.entity.Image;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 邮箱
     */
    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    private String email;

    /**
     * 手机号
     */
    @Desensitization(type = DesensitizationTypeEnum.MOBILE)
    private String phone;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    /**
     * 关注数量
     */
    private Long followingCount;

    /**
     * 粉丝数量
     */
    private Long followersCount;

    /**
     * 帖子数量
     */
    private Long postsCount;

    /**
     * 是否已关注该用户
     */
    private Boolean isFollowing;

    /**
     * 是否被该用户关注
     */
    private Boolean isFollowedBy;

    /**
     * 是否互相关注
     */
    private Boolean isMutualFollow;
}