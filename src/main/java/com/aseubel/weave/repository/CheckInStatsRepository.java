package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.user.CheckInStats;
import com.aseubel.weave.pojo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 签到统计Repository
 * @author Aseubel
 * @date 2025/6/29
 */
public interface CheckInStatsRepository extends JpaRepository<CheckInStats, Long> {

    /**
     * 根据用户查询签到统计
     */
    Optional<CheckInStats> findByUser(User user);

    /**
     * 根据用户ID查询签到统计
     */
    Optional<CheckInStats> findByUserId(Long userId);

    /**
     * 检查用户是否存在签到统计记录
     */
    boolean existsByUser(User user);
}