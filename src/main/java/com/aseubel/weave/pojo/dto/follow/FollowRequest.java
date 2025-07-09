package com.aseubel.weave.pojo.dto.follow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 关注请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
public class FollowRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId; // 要关注/取消关注的用户ID
}