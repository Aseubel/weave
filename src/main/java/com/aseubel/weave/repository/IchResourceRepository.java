package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.ich.Category;
import com.aseubel.weave.pojo.entity.ich.IchResource;
import com.aseubel.weave.pojo.entity.ich.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 非遗资源Repository
 * @author Aseubel
 * @date 2025/7/8
 */
@Repository
public interface IchResourceRepository extends JpaRepository<IchResource, Long> {

    /**
     * 根据分类查询资源
     */
    Page<IchResource> findByCategory(Category category, Pageable pageable);

    /**
     * 根据分类ID查询资源
     */
    Page<IchResource> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * 根据资源类型查询
     */
    Page<IchResource> findByType(ResourceType type, Pageable pageable);

    /**
     * 根据上传者查询资源
     */
    Page<IchResource> findByUploaderId(Long uploaderId, Pageable pageable);

    /**
     * 搜索资源（标题和描述）
     */
    @Query("SELECT r FROM IchResource r WHERE r.title LIKE %:keyword% OR r.description LIKE %:keyword%")
    Page<IchResource> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据分类和关键词搜索
     */
    @Query("SELECT r FROM IchResource r WHERE r.category.id = :categoryId AND (r.title LIKE %:keyword% OR r.description LIKE %:keyword%)")
    Page<IchResource> searchByCategoryAndKeyword(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据资源类型和关键词搜索
     */
    @Query("SELECT r FROM IchResource r WHERE r.type = :type AND (r.title LIKE %:keyword% OR r.description LIKE %:keyword%)")
    Page<IchResource> searchByTypeAndKeyword(@Param("type") ResourceType type, @Param("keyword") String keyword, Pageable pageable);

    /**
     * 获取热门资源（可以根据浏览量、点赞数等排序，这里先按创建时间倒序）
     */
    @Query("SELECT r FROM IchResource r ORDER BY r.createdAt DESC")
    List<IchResource> findHotResources(Pageable pageable);

    /**
     * 获取推荐资源（基于分类的简单推荐）
     */
    @Query("SELECT r FROM IchResource r WHERE r.category.id = :categoryId AND r.id != :excludeId ORDER BY r.createdAt DESC")
    List<IchResource> findRecommendedResources(@Param("categoryId") Long categoryId, @Param("excludeId") Long excludeId, Pageable pageable);

    /**
     * 统计分类下的资源数量
     */
    long countByCategory(Category category);

    /**
     * 统计分类下的资源数量（通过分类ID）
     */
    long countByCategoryId(Long categoryId);

    /**
     * 检查资源是否存在
     */
    boolean existsByIdAndUploaderId(Long id, Long uploaderId);
}