package com.aseubel.weave.pojo.entity.comment;

import com.aseubel.weave.pojo.entity.BaseEntity;
import com.aseubel.weave.pojo.entity.ich.IchResource;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.pojo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * 评论实体 - 支持对帖子和资源的评论
 * @author Aseubel
 * @date 2025/6/27 下午7:50
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user; // 评论者

    // 评论的帖子
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    // 评论的资源
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private IchResource resource;

    @Lob
    @Column(nullable = false)
    private String content; // 评论内容

    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0; // 评论点赞数

    // 自关联，用于实现回复功能
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> replies;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CommentStatus status = CommentStatus.PUBLISHED; // 评论状态

    // 评论状态枚举
    public enum CommentStatus {
        PUBLISHED, // 已发布
        HIDDEN,    // 隐藏
        DELETED    // 已删除
    }
}
