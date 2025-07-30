package com.aseubel.weave.pojo.entity.competition;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 参赛作品实体
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
@Table(name = "submission")
public class Submission extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String title; // 作品标题

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; // 作品描述

    private String contentUrl; // 作品内容的链接（如代码仓库、视频链接等）

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submissionTime = LocalDateTime.now(); // 提交时间

    private int voteCount = 0; // 作品获得的票数，默认为0

    // 多份作品可以属于同一个比赛
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    // 多份作品可以由同一个作者提交
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Participant author;

}