package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:06
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByMobile(String mobile);
    
    /**
     * 根据微信OpenID查找用户
     */
    Optional<User> findByWechatOpenId(String wechatOpenId);
    
    /**
     * 根据QQ OpenID查找用户
     */
    Optional<User> findByQqOpenId(String qqOpenId);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查手机号是否存在
     */
    boolean existsByMobile(String mobile);
    
    /**
     * 检查微信OpenID是否存在
     */
    boolean existsByWechatOpenId(String wechatOpenId);
    
    /**
     * 检查QQ OpenID是否存在
     */
    boolean existsByQqOpenId(String qqOpenId);
    
    /**
     * 根据用户名或手机号查找用户（用于登录）
     */
    @Query("SELECT u FROM User u WHERE u.username = :loginId OR u.mobile = :loginId")
    Optional<User> findByUsernameOrMobile(@Param("loginId") String loginId);
    
    /**
     * 根据关键字搜索用户（用户名、昵称、手机号模糊匹配）
     */
    Page<User> findByUsernameContainingOrNicknameContainingOrMobileContaining(
            String username, String nickname, String mobile, Pageable pageable);
}
