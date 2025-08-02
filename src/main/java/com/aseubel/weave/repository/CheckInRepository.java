package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.user.CheckIn;
import com.aseubel.weave.pojo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 签到记录Repository
 * @author Aseubel
 * @date 2025/6/29
 */
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    /**
     * 查询用户指定日期的签到记录
     */
    Optional<CheckIn> findByUserAndCheckDate(User user, LocalDate checkDate);

    /**
     * 查询用户指定日期范围内的签到记录
     */
    List<CheckIn> findByUserAndCheckDateBetweenOrderByCheckDateDesc(
            User user, LocalDate startDate, LocalDate endDate);

    /**
     * 查询用户最近的签到记录
     */
    Optional<CheckIn> findTopByUserOrderByCheckDateDesc(User user);

    /**
     * 统计用户总签到天数
     */
    @Query("SELECT COUNT(c) FROM CheckIn c WHERE c.user = :user")
    Long countByUser(@Param("user") User user);

    /**
     * 统计用户指定月份的签到天数
     */
    @Query("SELECT COUNT(c) FROM CheckIn c WHERE c.user = :user " +
           "AND YEAR(c.checkDate) = :year AND MONTH(c.checkDate) = :month")
    Long countByUserAndYearAndMonth(@Param("user") User user, 
                                   @Param("year") int year, 
                                   @Param("month") int month);

    /**
     * 统计用户指定年份的签到天数
     */
    @Query("SELECT COUNT(c) FROM CheckIn c WHERE c.user = :user " +
           "AND YEAR(c.checkDate) = :year")
    Long countByUserAndYear(@Param("user") User user, @Param("year") int year);

    /**
     * 查询用户连续签到记录（从指定日期往前查）
     */
    @Query("SELECT c FROM CheckIn c WHERE c.user = :user " +
           "AND c.checkDate <= :endDate " +
           "ORDER BY c.checkDate DESC")
    List<CheckIn> findConsecutiveCheckIns(@Param("user") User user, 
                                         @Param("endDate") LocalDate endDate);

    /**
     * 统计用户补签次数
     */
    @Query("SELECT COUNT(c) FROM CheckIn c WHERE c.user = :user AND c.isMakeUp = true")
    Long countMakeUpByUser(@Param("user") User user);

    /**
     * 查询用户指定日期是否已签到
     */
    boolean existsByUserAndCheckDate(User user, LocalDate checkDate);

    /**
     * 查询用户本月签到记录
     */
    @Query("SELECT c FROM CheckIn c WHERE c.user = :user " +
           "AND YEAR(c.checkDate) = :year AND MONTH(c.checkDate) = :month " +
           "ORDER BY c.checkDate ASC")
    List<CheckIn> findByUserAndYearAndMonth(@Param("user") User user, 
                                          @Param("year") int year, 
                                          @Param("month") int month);
}