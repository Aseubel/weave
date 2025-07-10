package com.aseubel.weave.service.impl;

import com.aseubel.weave.common.exception.BusinessException;
import com.aseubel.weave.exception.ResourceNotFoundException;
import com.aseubel.weave.pojo.dto.ich.CategoryRequest;
import com.aseubel.weave.pojo.dto.ich.CategoryResponse;
import com.aseubel.weave.pojo.entity.ich.Category;
import com.aseubel.weave.repository.CategoryRepository;
import com.aseubel.weave.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 * @author Aseubel
 * @date 2025/7/8
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // 验证父分类
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException("父分类不存在"));

            // 验证级别关系
            if (request.getLevel() != parent.getLevel() + 1) {
                throw new BusinessException("分类级别设置错误，应为父分类级别+1");
            }
        } else {
            // 顶级分类必须是级别1
            if (request.getLevel() != 1) {
                throw new BusinessException("顶级分类级别必须为1");
            }
        }

        // 检查分类名称在同级别下是否已存在
        validateCategoryName(request.getName(), request.getParentId(), null);

        // 检查分类编码是否已存在
        if (request.getCode() != null && categoryRepository.findByCode(request.getCode()).isPresent()) {
            throw new BusinessException("分类编码已存在");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parentId(request.getParentId())
                .level(request.getLevel())
                .code(request.getCode())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));

        // 验证父分类变更
        if (request.getParentId() != null && !request.getParentId().equals(category.getParentId())) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException("父分类不存在"));

            // 不能将分类设置为自己的子分类
            if (isDescendant(id, request.getParentId())) {
                throw new BusinessException("不能将分类设置为自己的子分类");
            }

            // 验证级别关系
            if (request.getLevel() != parent.getLevel() + 1) {
                throw new BusinessException("分类级别设置错误，应为父分类级别+1");
            }
        }

        // 检查分类名称在同级别下是否已存在（排除当前分类）
        validateCategoryName(request.getName(), request.getParentId(), id);

        // 检查分类编码是否已存在（排除当前分类）
        if (request.getCode() != null && categoryRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new BusinessException("分类编码已存在");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setParentId(request.getParentId());
        category.setLevel(request.getLevel());
        category.setCode(request.getCode());
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : category.getSortOrder());

        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));

        // 检查是否有子分类
        if (categoryRepository.existsByParentId(id)) {
            throw new BusinessException("该分类下还有子分类，无法删除");
        }

        // 检查分类下是否有资源
        Long resourceCount = categoryRepository.countResourcesByCategoryId(id);
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
        return categoryRepository.findAllByOrderByLevelAscSortOrderAscCreatedAtAsc().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getTopLevelCategories() {
        return categoryRepository.findByLevelOrderBySortOrderAscCreatedAtAsc(1).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getSubCategories(Long parentId) {
        return categoryRepository.findByParentIdOrderBySortOrderAscCreatedAtAsc(parentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getCategoryTree() {
        List<Category> topLevelCategories = categoryRepository.findByLevelOrderBySortOrderAscCreatedAtAsc(1);
        return topLevelCategories.stream()
                .map(this::convertToResponseWithChildren)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> searchCategories(String name) {
        return categoryRepository.findByNameContainingIgnoreCaseOrderBySortOrderAscCreatedAtAsc(name).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getCategoriesWithResources() {
        return categoryRepository.findCategoriesWithResources().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.findByName(name).isPresent();
    }

    /**
     * 验证分类名称在同级别下是否唯一
     */
    private void validateCategoryName(String name, Long parentId, Long excludeId) {
        List<Category> siblings = categoryRepository.findByParentIdOrderBySortOrderAscCreatedAtAsc(parentId);
        boolean exists = siblings.stream()
                .anyMatch(category -> category.getName().equals(name) &&
                        (excludeId == null || !category.getId().equals(excludeId)));
        if (exists) {
            throw new BusinessException("同级别下分类名称已存在");
        }
    }

    /**
     * 检查是否为子孙分类
     */
    private boolean isDescendant(Long ancestorId, Long descendantId) {
        if (ancestorId.equals(descendantId)) {
            return true;
        }

        List<Category> descendants = categoryRepository.findAllDescendants(ancestorId);
        return descendants.stream().anyMatch(category -> category.getId().equals(descendantId));
    }

    /**
     * 将实体转换为响应DTO
     */
    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParentId())
                .level(category.getLevel())
                .code(category.getCode())
                .sortOrder(category.getSortOrder())
                .fullPath(category.getFullPath())
                .isTopLevel(category.isTopLevel())
                .isLeaf(category.isLeaf())
                .resourceCount(categoryRepository.countResourcesByCategoryId(category.getId()))
                .createTime(category.getCreatedAt())
                .updateTime(category.getUpdatedAt())
                .build();

        // 设置父分类名称
        if (category.getParentId() != null) {
            categoryRepository.findById(category.getParentId())
                    .ifPresent(parent -> response.setParentName(parent.getName()));
        }

        return response;
    }

    /**
     * 将实体转换为响应DTO（包含子分类）
     */
    private CategoryResponse convertToResponseWithChildren(Category category) {
        CategoryResponse response = convertToResponse(category);

        // 递归获取子分类
        List<Category> children = categoryRepository.findByParentIdOrderBySortOrderAscCreatedAtAsc(category.getId())
                .stream()
                .toList();

        if (!children.isEmpty()) {
            List<CategoryResponse> childrenResponses = children.stream()
                    .map(this::convertToResponseWithChildren)
                    .collect(Collectors.toList());
            response.setChildren(childrenResponses);
        }

        return response;
    }
}