package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.ich.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分类Repository
 * @author Aseubel
 * @date 2025/7/8
 */
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

    /**
     * 根据编码查找分类
     */
    Optional<Category> findByCode(String code);

    /**
     * 检查分类名称是否存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, Long id);

    /**
     * 检查分类编码是否存在（排除指定ID）
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 根据父分类ID查找子分类
     */
    List<Category> findByParentIdOrderBySortOrderAscCreatedAtAsc(Long parentId);

    /**
     * 查找所有顶级分类（十大类）
     */
    List<Category> findByLevelOrderBySortOrderAscCreatedAtAsc(Integer level);

    /**
     * 查找所有分类
     */
    List<Category> findAllByOrderByLevelAscSortOrderAscCreatedAtAsc();

    /**
     * 根据名称模糊搜索分类
     */
    List<Category> findByNameContainingIgnoreCaseOrderBySortOrderAscCreatedAtAsc(String name);

    /**
     * 查找指定分类的所有子分类（递归）
     */
    @Query("SELECT c FROM Category c WHERE c.parentId = :parentId OR c.id IN " +
            "(SELECT c2.id FROM Category c2 WHERE c2.parentId IN " +
            "(SELECT c3.id FROM Category c3 WHERE c3.parentId = :parentId))")
    List<Category> findAllDescendants(@Param("parentId") Long parentId);

    /**
     * 统计分类下的资源数量
     */
    @Query("SELECT COUNT(r) FROM IchResource r WHERE r.id = :categoryId")
    Long countResourcesByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 查找分类树（包含子分类）
     */
    @Query("SELECT c FROM Category c " +
            "LEFT JOIN FETCH c.children " +
            "WHERE c.level = 1" +
            "ORDER BY c.sortOrder ASC, c.createdAt ASC")
    List<Category> findCategoryTreeWithChildren();

    /**
     * 检查分类是否有子分类
     */
    boolean existsByParentId(Long parentId);

    /**
     * 根据级别和父分类查找分类
     */
    List<Category> findByLevelAndParentIdOrderBySortOrderAscCreatedAtAsc(Integer level, Long parentId);
}