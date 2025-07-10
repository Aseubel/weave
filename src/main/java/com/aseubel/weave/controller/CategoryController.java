package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequirePermission;
import com.aseubel.weave.pojo.dto.ich.CategoryRequest;
import com.aseubel.weave.pojo.dto.ich.CategoryResponse;
import com.aseubel.weave.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 *
 * @author Aseubel
 * @date 2025/7/8
 */
@RestController
@RequestMapping("/api/ich/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "非遗分类相关接口（支持层级分类）")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @RequirePermission
    @Operation(summary = "创建分类", description = "创建新的分类，支持层级结构")
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "更新指定分类信息")
    public ApiResponse<CategoryResponse> updateCategory(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "删除指定分类（需确保无子分类和资源）")
    public ApiResponse<Void> deleteCategory(@Parameter(description = "分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详细信息")
    public ApiResponse<CategoryResponse> getCategoryById(@Parameter(description = "分类ID") @PathVariable Long id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ApiResponse.success(response);
    }

    @GetMapping
    @Operation(summary = "获取所有分类", description = "获取所有启用的分类列表")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ApiResponse.success(response);
    }

    @GetMapping("/top-level")
    @Operation(summary = "获取顶级分类", description = "获取非遗十大类分类")
    public ApiResponse<List<CategoryResponse>> getTopLevelCategories() {
        List<CategoryResponse> response = categoryService.getTopLevelCategories();
        return ApiResponse.success(response);
    }

    @GetMapping("/{parentId}/children")
    @Operation(summary = "获取子分类", description = "获取指定分类的所有子分类")
    public ApiResponse<List<CategoryResponse>> getSubCategories(
            @Parameter(description = "父分类ID") @PathVariable Long parentId) {
        List<CategoryResponse> response = categoryService.getSubCategories(parentId);
        return ApiResponse.success(response);
    }

    @GetMapping("/tree")
    @Operation(summary = "获取分类树", description = "获取完整的分类树结构")
    public ApiResponse<List<CategoryResponse>> getCategoryTree() {
        List<CategoryResponse> response = categoryService.getCategoryTree();
        return ApiResponse.success(response);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索分类", description = "根据名称模糊搜索分类")
    public ApiResponse<List<CategoryResponse>> searchCategories(
            @Parameter(description = "搜索关键词") @RequestParam String name) {
        List<CategoryResponse> response = categoryService.searchCategories(name);
        return ApiResponse.success(response);
    }

    @GetMapping("/with-resources")
    @Operation(summary = "获取有资源的分类", description = "获取包含资源的分类列表")
    public ApiResponse<List<CategoryResponse>> getCategoriesWithResources() {
        List<CategoryResponse> response = categoryService.getCategoriesWithResources();
        return ApiResponse.success(response);
    }

    @GetMapping("/exists")
    @Operation(summary = "检查分类名称是否存在", description = "验证分类名称的唯一性")
    public ApiResponse<Boolean> checkCategoryExists(
            @Parameter(description = "分类名称") @RequestParam String name) {
        boolean exists = categoryService.existsByName(name);
        return ApiResponse.success(exists);
    }
}