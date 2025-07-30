package com.aseubel.weave.pojo.entity.competition;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aseubel.weave.pojo.entity.BaseEntity;
import com.aseubel.weave.pojo.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

/**
 * 参赛者实体
 * @author Aseubel
 * @date 2025/7/29
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "participant")
public class Participant extends BaseEntity {

    /**
     * 与User实体建立一对一关联。每个参赛者身份都必须对应一个真实的用户。
     * unique = true 确保一个User只能有一个Participant身份。
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    @ToString.Exclude
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String profile; // 个人简介可以保留，作为参赛者在比赛中的特定信息

    // 一个参赛者可以有多份参赛作品
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Submission> submissions;

    // 一个参赛者可以参加多个比赛
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "competition_participant", // 中间表的名称
            joinColumns = @JoinColumn(name = "participant_id"), // 本实体在中间表的外键
            inverseJoinColumns = @JoinColumn(name = "competition_id") // 对方实体在中间表的外键
    )
    private Set<Competition> competitions;

    @Builder.Default
    private Boolean publicAccount = false; // 是否公开账号，默认不公开

    public Boolean isNew() {
        return ObjectUtil.isEmpty(super.getId()) && ObjectUtil.isEmpty(user);
    }

    public void hide() {
        if (BooleanUtil.isTrue(publicAccount)) {
            this.user = null;
        }
    }
}