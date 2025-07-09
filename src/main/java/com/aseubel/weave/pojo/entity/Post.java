package com.aseubel.weave.pojo.entity;

import com.aseubel.weave.pojo.entity.user.InterestTag;
import com.aseubel.weave.pojo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * 社区帖子实体
 * @author Aseubel
 * @date 2025/6/29
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 作者

    @Column(nullable = false, length = 200)
    private String title; // 标题

    @Lob
    @Column(nullable = false)
    private String content; // 内容

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostType type; // 帖子类型

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PostStatus status = PostStatus.PUBLISHED; // 帖子状态

    @Column(length = 1000)
    private String images; // 图片URLs，JSON格式存储

    @Column(length = 500)
    private String tags; // 标签，逗号分隔

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0; // 浏览次数

    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0; // 点赞数

    @Builder.Default
    @Column(nullable = false)
    private Integer commentCount = 0; // 评论数

    @Builder.Default
    @Column(nullable = false)
    private Integer shareCount = 0; // 分享数

    @Builder.Default
    @Column(nullable = false)
    private Boolean isTop = false; // 是否置顶

    @Builder.Default
    @Column(nullable = false)
    private Boolean isHot = false; // 是否热门

    // 关联的兴趣标签
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_interest_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<InterestTag> interestTags;

    // 帖子类型枚举
    public enum PostType {
        TEXT,      // 纯文本
        IMAGE,     // 图文
        VIDEO,     // 视频
        LINK,      // 链接分享
        QUESTION,  // 提问
        DISCUSSION // 讨论
    }

    // 帖子状态枚举
    public enum PostStatus {
        DRAFT,     // 草稿
        PUBLISHED, // 已发布
        HIDDEN,    // 隐藏
        DELETED    // 已删除
    }
}