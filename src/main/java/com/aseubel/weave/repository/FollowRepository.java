package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.Follow;
import com.aseubel.weave.pojo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 关注Repository
 * @author Aseubel
 * @date 2025/6/29
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 查询关注关系
     */
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    /**
     * 检查是否关注
     */
    boolean existsByFollowerAndFollowingAndStatus(User follower, User following, Follow.FollowStatus status);

    /**
     * 查询用户的关注列表
     */
    Page<Follow> findByFollowerAndStatusOrderByCreatedAtDesc(User follower, Follow.FollowStatus status, Pageable pageable);

    /**
     * 查询用户的粉丝列表
     */
    Page<Follow> findByFollowingAndStatusOrderByCreatedAtDesc(User following, Follow.FollowStatus status, Pageable pageable);

    /**
     * 统计关注数
     */
    long countByFollowerAndStatus(User follower, Follow.FollowStatus status);

    /**
     * 统计粉丝数
     */
    long countByFollowingAndStatus(User following, Follow.FollowStatus status);

    /**
     * 查询互相关注的用户
     */
    @Query("SELECT f1.following FROM Follow f1 WHERE f1.follower = :user AND f1.status = :status " +
           "AND EXISTS (SELECT f2 FROM Follow f2 WHERE f2.follower = f1.following AND f2.following = :user AND f2.status = :status)")
    List<User> findMutualFollows(@Param("user") User user, @Param("status") Follow.FollowStatus status);

    /**
     * 删除关注关系
     */
    void deleteByFollowerAndFollowing(User follower, User following);

    /**
     * 查询用户关注的人的ID列表
     */
    @Query("SELECT f.following.id FROM Follow f WHERE f.follower = :user AND f.status = :status")
    List<Long> findFollowingIds(@Param("user") User user, @Param("status") Follow.FollowStatus status);
}