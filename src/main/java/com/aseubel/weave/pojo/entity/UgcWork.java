package com.aseubel.weave.pojo.entity;

import com.aseubel.weave.pojo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:54
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ugc_work")
public class UgcWork extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User author;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private String contentUrl; // 作品链接（图片、3D模型文件等）

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UgcStatus status; // 审核状态

    public enum UgcStatus {
        PENDING, // 待审核
        APPROVED, // 已通过
        REJECTED  // 已拒绝
    }
}
