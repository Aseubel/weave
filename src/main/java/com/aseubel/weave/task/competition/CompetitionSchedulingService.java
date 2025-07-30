package com.aseubel.weave.task.competition;

import com.aseubel.weave.pojo.entity.competition.Competition;
import com.aseubel.weave.pojo.entity.competition.CompetitionState;
import com.aseubel.weave.repository.CompetitionRepository;
import com.aseubel.weave.service.CompetitionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025/7/29
 */
@Service
@Slf4j
public class CompetitionSchedulingService {

    @Resource
    private CompetitionRepository competitionRepository;

    @Resource
    private CompetitionService competitionService;

    /**
     * 每分钟执行一次，检查并开始到期的比赛
     * fixedRate = 60000 表示每 60 秒执行一次
     */
    @Scheduled(fixedRate = 60000)
    public void checkAndStartCompetitions() {
//        log.info("定时任务：检查需要开始的比赛...");
        List<Competition> competitionsToStart = competitionRepository.findByStateAndStartTimeBefore(
                CompetitionState.NOT_STARTED,
                LocalDateTime.now()
        );

        for (Competition competition : competitionsToStart) {
            log.info("比赛ID: {} '{}' 已到达开始时间，正在启动...", competition.getId(), competition.getName());
            try {
                competitionService.start(competition.getId());
                log.info("比赛ID: {} '{}' 成功启动。", competition.getId(), competition.getName());
            } catch (Exception e) {
                log.error("启动比赛ID: {} 时发生错误: {}", competition.getId(), e.getMessage());
            }
        }
    }

    /**
     * 每分钟执行一次，检查并结束到期的比赛
     */
    @Scheduled(fixedRate = 60000)
    public void checkAndEndCompetitions() {
//        log.info("定时任务：检查需要结束的比赛...");
        List<CompetitionState> runningStates = Arrays.asList(
                CompetitionState.PUBLIC_RUNNING,
                CompetitionState.HIDDEN_RUNNING
        );

        List<Competition> competitionsToEnd = competitionRepository.findByStateInAndEndTimeBefore(
                runningStates,
                LocalDateTime.now()
        );

        for (Competition competition : competitionsToEnd) {
            log.info("比赛ID: {} '{}' 已到达结束时间，正在终止...", competition.getId(), competition.getName());
            try {
                competitionService.end(competition.getId());
                log.info("比赛ID: {} '{}' 成功终止。", competition.getId(), competition.getName());
            } catch (Exception e) {
                log.error("终止比赛ID: {} 时发生错误: {}", competition.getId(), e.getMessage());
            }
        }
    }
}