package com.aseubel.weave.pojo.dto.competition;

import com.aseubel.weave.pojo.entity.competition.Participant;
import com.aseubel.weave.pojo.entity.competition.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025/7/30 下午2:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionResponse {
    private Long competitionId;

    private Long submissionId;

    private String title; // 作品标题

    private String description; // 作品描述

    private String contentUrl; // 作品内容的链接（如代码仓库、视频链接等）

    private Integer voteCount; // 作品的投票数

    private ParticipantResponse author;

    public static SubmissionResponse fromSubmission(Submission submission) {
        return SubmissionResponse.builder()
                .competitionId(submission.getCompetition().getId())
                .submissionId(submission.getId())
                .title(submission.getTitle())
                .description(submission.getDescription())
                .contentUrl(submission.getContentUrl())
                .voteCount(submission.getVoteCount())
                .author(ParticipantResponse.fromParticipant(submission.getAuthor()))
                .build();
    }
}
