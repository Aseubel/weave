package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.ich.IchResourceRequest;
import com.aseubel.weave.pojo.dto.ich.IchResourceResponse;
import com.aseubel.weave.pojo.entity.ich.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 非遗资源服务接口
 * @author Aseubel
 * @date 2025/7/8
 */
public interface IchResourceService {

    /**
     * 创建资源
     */
    IchResourceResponse createResource(IchResourceRequest request, Long uploaderId);

    /**
     * 更新资源
     */
    IchResourceResponse updateResource(Long id, IchResourceRequest request, Long uploaderId);

    /**
     * 删除资源
     */
    void deleteResource(Long id, Long uploaderId);

    /**
     * 根据ID获取资源详情
     */
    IchResourceResponse getResourceById(Long id);

    /**
     * 分页获取所有资源
     */
    Page<IchResourceResponse> getAllResources(Pageable pageable);

    /**
     * 根据分类ID分页获取资源
     */
    Page<IchResourceResponse> getResourcesByCategory(Long categoryId, Pageable pageable);

    /**
     * 根据资源类型分页获取资源
     */
    Page<IchResourceResponse> getResourcesByType(ResourceType type, Pageable pageable);

    /**
     * 根据上传者ID分页获取资源
     */
    Page<IchResourceResponse> getResourcesByUploader(Long uploaderId, Pageable pageable);

    /**
     * 搜索资源
     */
    Page<IchResourceResponse> searchResources(String keyword, Pageable pageable);

    /**
     * 根据分类和关键词搜索资源
     */
    Page<IchResourceResponse> searchResourcesByCategory(Long categoryId, String keyword, Pageable pageable);

    /**
     * 根据资源类型和关键词搜索资源
     */
    Page<IchResourceResponse> searchResourcesByType(ResourceType type, String keyword, Pageable pageable);

    /**
     * 获取热门资源
     */
    List<IchResourceResponse> getHotResources(int limit);

    /**
     * 获取推荐资源
     */
    List<IchResourceResponse> getRecommendedResources(Long resourceId, int limit);
}