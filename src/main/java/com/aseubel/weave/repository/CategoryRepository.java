package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.ich.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分类Repository
 * @author Aseubel
 * @date 2025/7/8
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 根据名称查找分类
     */
    Optional<Category> findByName(String name);

    /**
     * 检查分类名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 获取所有分类（按名称排序）
     */
    List<Category> findAllByOrderByName();

    /**
     * 根据名称模糊查询分类
     */
    List<Category> findByNameContainingIgnoreCase(String name);

    /**
     * 获取有资源的分类
     */
    @Query("SELECT DISTINCT c FROM Category c WHERE EXISTS (SELECT 1 FROM IchResource r WHERE r.category = c)")
    List<Category> findCategoriesWithResources();
}