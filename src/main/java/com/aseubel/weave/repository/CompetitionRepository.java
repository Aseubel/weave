package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.competition.Competition;
import com.aseubel.weave.pojo.entity.competition.CompetitionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025/7/28 下午5:23
 */
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    @Transactional
    @Modifying
    @Query("update Competition c set c.deleted = ?1 where c.id = ?2")
    void updateDeletedById(Boolean deleted, Long id);

    /**
     * 查找所有状态为 NOT_STARTED 且开始时间已过的比赛
     * @param state 比赛状态
     * @param now 当前时间
     * @return 需要开始的比赛列表
     */
    List<Competition> findByStateAndStartTimeBefore(CompetitionState state, LocalDateTime now);

    /**
     * 查找所有正在进行中且结束时间已过的比赛
     * @param states 比赛状态列表
     * @param now 当前时间
     * @return 需要结束的比赛列表
     */
    List<Competition> findByStateInAndEndTimeBefore(List<CompetitionState> states, LocalDateTime now);
}
