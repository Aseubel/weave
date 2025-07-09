package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.user.InterestTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 兴趣标签Repository
 * @author Aseubel
 * @date 2025/6/27
 */
public interface InterestTagRepository extends JpaRepository<InterestTag, Long> {
    
    /**
     * 根据ID列表查找兴趣标签
     */
    List<InterestTag> findAllById(Iterable<Long> ids);
    
    /**
     * 根据名称查找兴趣标签
     */
    InterestTag findByName(String name);
    
    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);
}