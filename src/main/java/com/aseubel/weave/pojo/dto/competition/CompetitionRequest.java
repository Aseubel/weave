package com.aseubel.weave.pojo.dto.competition;

import com.aseubel.weave.pojo.entity.competition.Competition;
import com.aseubel.weave.pojo.entity.competition.CompetitionState;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025/7/30 下午1:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionRequest implements Serializable {

    @NotNull(message = "比赛名称不能为空")
    private String name;

    @NotNull(message = "比赛描述不能为空")
    private String description;

    @NotNull(message = "比赛发起者/组织者不能为空")
    private String organizer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @NotNull(message = "比赛规则不能为空")
    private String rules;

    @NotNull(message = "联系方式不能为空")
    private String contact;

    public Competition toCompetition() {
        return Competition.builder()
                .name(name)
                .description(description)
                .organizer(organizer)
                .startTime(startTime)
                .endTime(endTime)
                .rules(rules)
                .state(CompetitionState.NOT_STARTED)
                .contact(contact)
                .build();
    }

}
