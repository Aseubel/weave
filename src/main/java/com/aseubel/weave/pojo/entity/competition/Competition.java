package com.aseubel.weave.pojo.entity.competition;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025/7/28 下午4:25
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "competition")
public class Competition extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String organizer;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String rules;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    @Convert(converter = CompetitionState.CompetitionStateConverter.class)
    private CompetitionState state;

    public Competition(Competition competition) {
        Competition.builder()
                .name(competition.getName())
                .description(competition.getDescription())
                .organizer(competition.getOrganizer())
                .startTime(competition.getStartTime())
                .endTime(competition.getEndTime())
                .rules(competition.getRules())
                .contact(competition.getContact())
                .build();
    }

    public Competition(Long competitionId) {
        super.setId(competitionId);
    }
}
