package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.ich.IchResourceRequest;
import com.aseubel.weave.pojo.dto.ich.IchResourceResponse;
import com.aseubel.weave.pojo.entity.ich.ResourceType;
import com.aseubel.weave.service.IchResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 非遗资源控制器
 *
 * @author Aseubel
 * @date 2025/1/15
 */
@RestController
@RequestMapping("/api/ich/resources")
@RequiredArgsConstructor
public class IchResourceController {

    private final IchResourceService ichResourceService;

    @PostMapping
    public ApiResponse<IchResourceResponse> createResource(
            @Valid @RequestBody IchResourceRequest request) {
        Long userId = UserContext.getCurrentUserId();
        IchResourceResponse response = ichResourceService.createResource(request, userId);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<IchResourceResponse> updateResource(
            @PathVariable Long id,
            @Valid @RequestBody IchResourceRequest request) {
        Long userId = UserContext.getCurrentUserId();
        IchResourceResponse response = ichResourceService.updateResource(id, request, userId);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteResource(@PathVariable Long id) {
        Long userId = UserContext.getCurrentUserId();
        ichResourceService.deleteResource(id, userId);
        return ApiResponse.success();
    }

    @GetMapping("/{id}")
    public ApiResponse<IchResourceResponse> getResourceById(@PathVariable Long id) {
        IchResourceResponse response = ichResourceService.getResourceById(id);
        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<Page<IchResourceResponse>> getAllResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<IchResourceResponse> response = ichResourceService.getAllResources(pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<Page<IchResourceResponse>> getResourcesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<IchResourceResponse> response = ichResourceService.getResourcesByCategory(categoryId, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/type/{type}")
    public ApiResponse<Page<IchResourceResponse>> getResourcesByType(
            @PathVariable ResourceType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<IchResourceResponse> response = ichResourceService.getResourcesByType(type, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/uploader/{uploaderId}")
    public ApiResponse<Page<IchResourceResponse>> getResourcesByUploader(
            @PathVariable Long uploaderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<IchResourceResponse> response = ichResourceService.getResourcesByUploader(uploaderId, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/search")
    public ApiResponse<Page<IchResourceResponse>> searchResources(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<IchResourceResponse> response = ichResourceService.searchResources(keyword, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/search/category/{categoryId}")
    public ApiResponse<Page<IchResourceResponse>> searchResourcesByCategory(
            @PathVariable Long categoryId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<IchResourceResponse> response = ichResourceService.searchResourcesByCategory(categoryId, keyword,
                pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/search/type/{type}")
    public ApiResponse<Page<IchResourceResponse>> searchResourcesByType(
            @PathVariable ResourceType type,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<IchResourceResponse> response = ichResourceService.searchResourcesByType(type, keyword, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/hot")
    public ApiResponse<List<IchResourceResponse>> getHotResources(
            @RequestParam(defaultValue = "10") int limit) {
        List<IchResourceResponse> response = ichResourceService.getHotResources(limit);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}/recommended")
    public ApiResponse<List<IchResourceResponse>> getRecommendedResources(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int limit) {
        List<IchResourceResponse> response = ichResourceService.getRecommendedResources(id, limit);
        return ApiResponse.success(response);
    }
}