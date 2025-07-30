package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.competition.Competition;
import com.aseubel.weave.pojo.entity.competition.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/7/29 上午11:35
 */
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    /**
     * 增加投票数
     */
    @Modifying
    @Query("UPDATE Submission p SET p.voteCount = p.voteCount + :increment WHERE p.id = :submissionId")
    void updateVoteCount(@Param("submissionId") Long submissionId, @Param("increment") Long increment);

    /**
     * 根据比赛id查询所有提交
     */
    List<Submission> findAllByCompetition(Competition competition);
}
