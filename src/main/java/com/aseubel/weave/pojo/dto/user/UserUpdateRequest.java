package com.aseubel.weave.pojo.dto.user;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * 用户信息更新请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
public class UserUpdateRequest {

    @Size(max = 50, message = "昵称不能超过50个字符")
    private String nickname; // 昵称

    @Size(max = 200, message = "个人简介不能超过200个字符")
    private String bio; // 个人简介

    @Past(message = "生日必须是过去的日期")
    private LocalDate birthday; // 生日

    @Pattern(regexp = "^(男|女|其他)$", message = "性别只能是：男、女、其他")
    private String gender; // 性别

    @Size(max = 100, message = "城市不能超过100个字符")
    private String city; // 城市

    @Size(max = 100, message = "职业不能超过100个字符")
    private String profession; // 职业

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱不能超过100个字符")
    private String email; // 邮箱

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile; // 手机号

    private Set<Long> interestTagIds; // 兴趣标签ID集合

    private String avatarId; // 头像id
}