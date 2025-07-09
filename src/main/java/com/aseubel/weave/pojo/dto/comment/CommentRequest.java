package com.aseubel.weave.pojo.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 评论请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
public class CommentRequest {

    private Long postId; // 帖子ID（评论帖子时必填）

    private Long resourceId; // 资源ID（评论资源时必填）

    private Long parentId; // 父评论ID（回复评论时必填）

    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论内容长度不能超过1000字符")
    private String content;
}