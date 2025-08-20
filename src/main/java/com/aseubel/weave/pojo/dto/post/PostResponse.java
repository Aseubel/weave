package com.aseubel.weave.pojo.dto.post;

import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 帖子响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private String id;
    private String title;
    private String content;
    private Post.PostType type;
    private Post.PostStatus status;
    private List<String> images;
    private List<String> tags;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Boolean isTop;
    private Boolean isHot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 作者信息
    private AuthorInfo author;

    // 兴趣标签信息
    private Set<InterestTagInfo> interestTags;

    // 当前用户是否点赞
    private Boolean isLiked;

    // 当前用户是否关注作者
    private Boolean isFollowingAuthor;

    /**
     * 作者信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorInfo {
        private String id;
        private String username;
        private String nickname;
        private String avatar;
        private Integer level;
        private Boolean isActive;
    }

    /**
     * 兴趣标签信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterestTagInfo {
        private String id;
        private String name;
        private String color;
    }
}