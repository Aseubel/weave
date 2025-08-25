package com.aseubel.weave.pojo.dto.comment;

import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private String id;
    private String content;
    private Integer likeCount;
    private Comment.CommentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 评论者信息
    private UserInfo user;

    // 父评论信息（如果是回复）
    private ParentCommentInfo parent;

    // 回复列表
    private List<CommentResponse> replies;

    // 回复数量
    private Integer replyCount;

    // 当前用户是否点赞
    private Boolean isLiked;

    /**
     * 用户信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String id;
        private String username;
        private String nickname;
        private Image avatar;
        private Integer level;
    }

    /**
     * 父评论信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParentCommentInfo {
        private String id;
        private String content;
        private UserInfo user;
    }
}