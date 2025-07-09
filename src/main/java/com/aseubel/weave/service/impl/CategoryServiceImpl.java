package com.aseubel.weave.service.impl;

import com.aseubel.weave.common.exception.BusinessException;
import com.aseubel.weave.exception.ResourceNotFoundException;
import com.aseubel.weave.pojo.dto.ich.CategoryRequest;
import com.aseubel.weave.pojo.dto.ich.CategoryResponse;
import com.aseubel.weave.pojo.entity.ich.Category;
import com.aseubel.weave.repository.CategoryRepository;
import com.aseubel.weave.repository.IchResourceRepository;
import com.aseubel.weave.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 * @author Aseubel
 * @date 2025/1/15
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final IchResourceRepository ichResourceRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // 检查分类名称是否已存在
        if (categoryRepository.existsByName(request.getName())) {
            throw new BusinessException("分类名称已存在");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));

        // 检查分类名称是否已存在（排除当前分类）
        if (!category.getName().equals(request.getName()) && 
            categoryRepository.existsByName(request.getName())) {
            throw new BusinessException("分类名称已存在");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return convertToResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));

        // 检查是否有资源使用此分类
        long resourceCount = ichResourceRepository.countByCategory(category);
        if (resourceCount > 0) {
            throw new BusinessException("该分类下还有资源，无法删除");
        }

        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));
        return convertToResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByName();
        return categories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getCategoriesWithResources() {
        List<Category> categories = categoryRepository.findCategoriesWithResources();
        return categories.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * 将实体转换为响应DTO
     */
    private CategoryResponse convertToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .resourceCount(ichResourceRepository.countByCategory(category))
                .build();
    }
}