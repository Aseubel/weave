package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.ich.CategoryRequest;
import com.aseubel.weave.pojo.dto.ich.CategoryResponse;

import java.util.List;

/**
 * 分类服务接口 - 支持层级分类
 * @author Aseubel
 * @date 2025/7/8
 */
public interface CategoryService {

    /**
     * 创建分类
     */
    CategoryResponse createCategory(CategoryRequest request);

    /**
     * 更新分类
     */
    CategoryResponse updateCategory(Long id, CategoryRequest request);

    /**
     * 删除分类
     */
    void deleteCategory(Long id);

    /**
     * 根据ID获取分类
     */
    CategoryResponse getCategoryById(Long id);

    /**
     * 获取所有分类
     */
    List<CategoryResponse> getAllCategories();

    /**
     * 获取顶级分类（十大类）
     */
    List<CategoryResponse> getTopLevelCategories();

    /**
     * 获取指定分类的子分类
     */
    List<CategoryResponse> getSubCategories(Long parentId);

    /**
     * 获取分类树（包含所有层级）
     */
    List<CategoryResponse> getCategoryTree();

    /**
     * 根据名称搜索分类
     */
    List<CategoryResponse> searchCategories(String name);

    /**
     * 获取有资源的分类
     */
    List<CategoryResponse> getCategoriesWithResources();

    /**
     * 检查分类名称是否存在
     */
    boolean existsByName(String name);
}