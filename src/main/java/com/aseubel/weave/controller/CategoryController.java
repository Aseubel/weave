package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.pojo.dto.ich.CategoryRequest;
import com.aseubel.weave.pojo.dto.ich.CategoryResponse;
import com.aseubel.weave.service.CategoryService;
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
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ApiResponse.success(response);
    }

    @GetMapping("/search")
    public ApiResponse<List<CategoryResponse>> searchCategoriesByName(@RequestParam String name) {
        List<CategoryResponse> response = categoryService.searchCategoriesByName(name);
        return ApiResponse.success(response);
    }

    @GetMapping("/with-resources")
    public ApiResponse<List<CategoryResponse>> getCategoriesWithResources() {
        List<CategoryResponse> response = categoryService.getCategoriesWithResources();
        return ApiResponse.success(response);
    }

    @GetMapping("/check-name")
    public ApiResponse<Boolean> checkNameExists(@RequestParam String name) {
        boolean exists = categoryService.existsByName(name);
        return ApiResponse.success(exists);
    }
}