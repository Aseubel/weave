package com.aseubel.weave.pojo.entity;

import com.aseubel.weave.pojo.entity.ich.IchResource;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:50
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 评论者

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private IchResource resource; // 评论的资源

    @Lob
    @Column(nullable = false)
    private String content; // 评论内容

    // 自关联，用于实现回复功能
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> replies;
}
