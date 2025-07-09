package com.aseubel.weave.pojo.entity.ich;

import com.aseubel.weave.pojo.entity.BaseEntity;
import com.aseubel.weave.pojo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:28
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ich_resource")
public class IchResource extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @Lob // 用于存储大文本
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING) // 枚举以字符串形式存储
    @Column(nullable = false, length = 20)
    private ResourceType type; // 资源类型: STATIC, DYNAMIC

    @Column(nullable = false)
    private String contentUrl; // 资源链接（图片、视频、音频）

    @Column(length = 100)
    private String copyrightInfo; // 版权信息

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 所属分类

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader; // 上传者（管理员）
}
