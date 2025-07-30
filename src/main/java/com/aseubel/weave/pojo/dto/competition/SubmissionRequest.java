package com.aseubel.weave.pojo.dto.competition;

import com.aseubel.weave.pojo.entity.competition.Competition;
import com.aseubel.weave.pojo.entity.competition.Submission;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/7/29 下午3:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionRequest implements Serializable {

    @NotNull(message = "竞赛ID不能为空")
    public Long competitionId;

    @NotNull(message = "作品标题不能为空")
    private String title;

    @NotNull(message = "作品描述不能为空")
    private String description;

    @NotNull(message = "作品内容不能为空")
    private String contentUrl;

    public Submission toSubmission() {
        return Submission.builder()
                .competition(new Competition(competitionId))
                .title(title)
                .description(description)
                .contentUrl(contentUrl)
                .build();
    }
}
