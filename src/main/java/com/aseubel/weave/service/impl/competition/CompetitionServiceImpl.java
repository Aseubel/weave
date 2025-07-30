package com.aseubel.weave.service.impl.competition;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.cola.statemachine.StateMachine;
import com.aseubel.weave.common.disruptor.DisruptorProducer;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.competition.CompetitionRequest;
import com.aseubel.weave.pojo.dto.competition.SubmissionRequest;
import com.aseubel.weave.pojo.entity.competition.*;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.CompetitionRepository;
import com.aseubel.weave.repository.ParticipantRepository;
import com.aseubel.weave.repository.SubmissionRepository;
import com.aseubel.weave.service.CompetitionService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/7/28 下午5:37
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final SubmissionRepository submissionRepository;
    private final ParticipantRepository participantRepository;
    private final StringRedisTemplate redisTemplate;
    private final DisruptorProducer disruptorProducer;

    @Value("${app.competition.vote.limit:3}")
    private Long voteLimit;

    @Resource
    private StateMachine<CompetitionState, CompetitionEvent, Void> competitionStateMachine;


    @Override
    public Competition create(CompetitionRequest request) {
        Competition competition = request.toCompetition();
        competitionRepository.save(competition);
        return competition;
    }

    @Override
    public Competition start(Long id) {
        Optional<Competition> competitionOptional = competitionRepository.findById(id);
        if (competitionOptional.isPresent()) {
            Competition competition = competitionOptional.get();
            CompetitionState nextState = competitionStateMachine.fireEvent(competition.getState(), CompetitionEvent.START, null);
            competition.setState(nextState);
            competitionRepository.save(competition);
            return competition;
        }
        return null;
    }

    @Override
    public Competition end(Long id) {
        Optional<Competition> competitionOptional = competitionRepository.findById(id);
        if (competitionOptional.isPresent()) {
            Competition competition = competitionOptional.get();
            CompetitionState nextState = competitionStateMachine.fireEvent(competition.getState(), CompetitionEvent.END, null);
            competition.setState(nextState);
            competitionRepository.save(competition);
            return competition;
        }
        return null;
    }

    @Override
    public Competition delete(Long id) {
        Optional<Competition> competitionOptional = competitionRepository.findById(id);
        if (competitionOptional.isEmpty()) {
            throw new IllegalArgumentException("未找到编号为 " + id + " 的比赛");
        }
        competitionRepository.updateDeletedById(true, id);
        return null;
    }

    @Override
    public Competition get(Long id) {
        return competitionRepository.findById(id).orElse(null);
    }

    @Override
    public Competition update(Long id, Competition competition) {
        Optional<Competition> competitionOptional = competitionRepository.findById(id);
        if (competitionOptional.isEmpty()) {
            return null;
        }
        Competition newCompetition = new Competition(competitionOptional.get());
        competitionRepository.save(newCompetition);
        return newCompetition;
    }

    @Override
    public Competition publicVoting(Long id) {
        Optional<Competition> competitionOptional = competitionRepository.findById(id);
        if (competitionOptional.isEmpty()) {
            return null;
        }
        Competition competition = competitionOptional.get();
        CompetitionState nextState = competitionStateMachine.fireEvent(competition.getState(), CompetitionEvent.PUBLIC_VOTING, null);
        competition.setState(nextState);
        competitionRepository.save(competition);
        return competition;
    }

    @Override
    public Competition hideVoting(Long id) {
        Optional<Competition> competitionOptional = competitionRepository.findById(id);
        if (competitionOptional.isEmpty()) {
            return null;
        }
        Competition competition = competitionOptional.get();
        CompetitionState nextState = competitionStateMachine.fireEvent(competition.getState(), CompetitionEvent.HIDE_VOTING, null);
        competition.setState(nextState);
        competitionRepository.save(competition);
        return competition;
    }

    @Override
    public List<Competition> getAll() {
        return competitionRepository.findAll();
    }

    @Override
    public Submission vote(Long competitionId, Long submissionId) {
        competitionRepository.findById(competitionId).ifPresent(competition -> {
                    if (competition.getState() == CompetitionState.NOT_STARTED) {
                        throw new IllegalArgumentException("比赛尚未开始");
                    }
                    if (competition.getState() == CompetitionState.ENDED) {
                        throw new IllegalArgumentException("比赛已结束");
                    }
                });

        User currentUser = UserContext.getCurrentUser();
        Long userId = currentUser.getId();
        // 记录被投票的作品
        redisTemplate.opsForSet().add(KeyBuilder.submissionVoteRecentKey(), String.valueOf(userId));

        // 添加投票数
        redisTemplate.opsForHash().increment(KeyBuilder.competitionVoteStatusKey(competitionId), userId, 1L);
        redisTemplate.opsForHash().increment(KeyBuilder.submissionVoteCountKey(), submissionId, 1);

        return submissionRepository.findById(submissionId).orElse(null);
    }

    private Long todayVoteCount(Long competitionId) {
        Long userId = UserContext.getCurrentUserId();
        return (Long) redisTemplate.opsForHash().get(KeyBuilder.competitionVoteStatusKey(competitionId), String.valueOf(userId));
    }

    @Override
    public List<Submission> getSubmissions(Long competitionId) {
        return submissionRepository.findAllByCompetition(new Competition(competitionId));
    }

    @Override
    public Long getRemainingVotes(Long competitionId) {
        return Optional.ofNullable(todayVoteCount(competitionId))
                .map(count -> voteLimit - count)
                .orElse(voteLimit);
    }

    @Override
    public Participant modifyParticipant(Participant participant) {
        User user = UserContext.getCurrentUser();

        if (participant.isNew()) {
            participant.setUser(user);
            return participantRepository.save(participant);
        }

        participantRepository
                .findOne(Example.of(Participant.builder().user(user).build()))
                .ifPresentOrElse(
                        p -> {
                            if (!p.getId().equals(participant.getId())) {
                                throw new IllegalArgumentException("不能修改其他用户的参赛信息");
                            }
                            participantRepository.save(participant);
                        },
                        () -> {
                            participant.setUser(user);
                            participantRepository.save(participant);
                        }
                );

        return participant;
    }

    @Override
    public Participant getParticipant() {
        User user = UserContext.getCurrentUser();
        return participantRepository.findByUserId(user.getId());
    }

    @Override
    public Submission createSubmission(SubmissionRequest request) {
        Submission submission = request.toSubmission();
        Competition competition = submission.getCompetition();

        competitionRepository.findById(competition.getId()).ifPresent(c -> {
            if (c.getState() == CompetitionState.NOT_STARTED) {
                throw new IllegalArgumentException("比赛尚未开始");
            }
            if (c.getState() == CompetitionState.ENDED) {
                throw new IllegalArgumentException("比赛已结束");
            }
        });

        User user = UserContext.getCurrentUser();
        Participant participant = participantRepository.findByUserId(user.getId());
        if (ObjectUtil.isEmpty(participant)) {
            throw new IllegalArgumentException("请先填写参赛信息");
        }
        submission.setAuthor(participant);
        submissionRepository.save(submission);
        return submission;
    }

    @Override
    public Submission getSubmission(Long submissionId) {
        return submissionRepository.findById(submissionId).orElse(null);
    }

}