package com.aseubel.weave.pojo.entity;


import com.aseubel.weave.common.annotation.SnowflakeId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025/6/27 下午6:35
 */
@Getter
@Setter
@MappedSuperclass // 表示这是一个基类，其属性会映射到子类的表中
@EntityListeners(AuditingEntityListener.class) // 启用JPA审计功能（自动填充创建/更新时间）
public abstract class BaseEntity implements Serializable {

    @Id
    @SnowflakeId // 自定义ID生成器
    private Long id;

    @CreatedDate // 实体创建时自动填充
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate // 实体更新时自动填充
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
