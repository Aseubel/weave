package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.competition.CompetitionRequest;
import com.aseubel.weave.pojo.dto.competition.SubmissionRequest;
import com.aseubel.weave.pojo.entity.competition.Competition;
import com.aseubel.weave.pojo.entity.competition.Participant;
import com.aseubel.weave.pojo.entity.competition.Submission;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/7/28 下午5:37
 */
public interface CompetitionService {
    Competition create(CompetitionRequest request);

    Competition start(Long id);

    Competition end(Long id);

    Competition delete(Long id);

    Competition get(Long id);

    Competition update(Long id, Competition competition);

    Competition publicVoting(Long id);

    Competition hideVoting(Long id);

    List<Competition> getAll();

    Submission vote(Long competitionId, Long submissionId);

    List<Submission> getSubmissions(Long competitionId);

    Long getRemainingVotes(Long competitionId);

    Participant modifyParticipant(Participant participant);

    Participant getParticipant();

    Submission createSubmission(SubmissionRequest request);

    Submission getSubmission(Long submissionId);
}
