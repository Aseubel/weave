package com.aseubel.weave.service.impl;

import com.aseubel.weave.exception.ResourceNotFoundException;
import com.aseubel.weave.exception.UnauthorizedException;
import com.aseubel.weave.pojo.dto.ich.CategoryResponse;
import com.aseubel.weave.pojo.dto.ich.IchResourceRequest;
import com.aseubel.weave.pojo.dto.ich.IchResourceResponse;
import com.aseubel.weave.pojo.entity.ich.Category;
import com.aseubel.weave.pojo.entity.ich.IchResource;
import com.aseubel.weave.pojo.entity.ich.ResourceType;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.repository.CategoryRepository;
import com.aseubel.weave.repository.IchResourceRepository;
import com.aseubel.weave.repository.UserRepository;
import com.aseubel.weave.service.IchResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 非遗资源服务实现类
 * @author Aseubel
 * @date 2025/1/15
 */
@Service
@RequiredArgsConstructor
public class IchResourceServiceImpl implements IchResourceService {

    private final IchResourceRepository ichResourceRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public IchResourceResponse createResource(IchResourceRequest request, Long uploaderId) {
        User uploader = userRepository.findById(uploaderId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));

        IchResource resource = IchResource.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .contentUrl(request.getContentUrl())
                .copyrightInfo(request.getCopyrightInfo())
                .category(category)
                .uploader(uploader)
                .build();

        IchResource savedResource = ichResourceRepository.save(resource);
        return convertToResponse(savedResource);
    }

    @Override
    @Transactional
    public IchResourceResponse updateResource(Long id, IchResourceRequest request, Long uploaderId) {
        IchResource resource = ichResourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("资源不存在"));

        // 检查是否为资源上传者
        if (!resource.getUploader().getId().equals(uploaderId)) {
            throw new UnauthorizedException("无权修改此资源");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));

        resource.setTitle(request.getTitle());
        resource.setDescription(request.getDescription());
        resource.setType(request.getType());
        resource.setContentUrl(request.getContentUrl());
        resource.setCopyrightInfo(request.getCopyrightInfo());
        resource.setCategory(category);

        IchResource updatedResource = ichResourceRepository.save(resource);
        return convertToResponse(updatedResource);
    }

    @Override
    @Transactional
    public void deleteResource(Long id, Long uploaderId) {
        IchResource resource = ichResourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("资源不存在"));

        // 检查是否为资源上传者
        if (!resource.getUploader().getId().equals(uploaderId)) {
            throw new UnauthorizedException("无权删除此资源");
        }

        ichResourceRepository.delete(resource);
    }

    @Override
    public IchResourceResponse getResourceById(Long id) {
        IchResource resource = ichResourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("资源不存在"));
        return convertToResponse(resource);
    }

    @Override
    public Page<IchResourceResponse> getAllResources(Pageable pageable) {
        Page<IchResource> resourcePage = ichResourceRepository.findAll(pageable);
        return convertToResponsePage(resourcePage);
    }

    @Override
    public Page<IchResourceResponse> getResourcesByCategory(Long categoryId, Pageable pageable) {
        Page<IchResource> resourcePage = ichResourceRepository.findByCategoryId(categoryId, pageable);
        return convertToResponsePage(resourcePage);
    }

    @Override
    public Page<IchResourceResponse> getResourcesByType(ResourceType type, Pageable pageable) {
        Page<IchResource> resourcePage = ichResourceRepository.findByType(type, pageable);
        return convertToResponsePage(resourcePage);
    }

    @Override
    public Page<IchResourceResponse> getResourcesByUploader(Long uploaderId, Pageable pageable) {
        Page<IchResource> resourcePage = ichResourceRepository.findByUploaderId(uploaderId, pageable);
        return convertToResponsePage(resourcePage);
    }

    @Override
    public Page<IchResourceResponse> searchResources(String keyword, Pageable pageable) {
        Page<IchResource> resourcePage = ichResourceRepository.searchByKeyword(keyword, pageable);
        return convertToResponsePage(resourcePage);
    }

    @Override
    public Page<IchResourceResponse> searchResourcesByCategory(Long categoryId, String keyword, Pageable pageable) {
        Page<IchResource> resourcePage = ichResourceRepository.searchByCategoryAndKeyword(categoryId, keyword, pageable);
        return convertToResponsePage(resourcePage);
    }

    @Override
    public Page<IchResourceResponse> searchResourcesByType(ResourceType type, String keyword, Pageable pageable) {
        Page<IchResource> resourcePage = ichResourceRepository.searchByTypeAndKeyword(type, keyword, pageable);
        return convertToResponsePage(resourcePage);
    }

    @Override
    public List<IchResourceResponse> getHotResources(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<IchResource> resources = ichResourceRepository.findHotResources(pageable);
        return resources.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<IchResourceResponse> getRecommendedResources(Long resourceId, int limit) {
        IchResource resource = ichResourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("资源不存在"));

        Pageable pageable = PageRequest.of(0, limit);
        List<IchResource> resources = ichResourceRepository.findRecommendedResources(
                resource.getCategory().getId(), resourceId, pageable);

        return resources.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将实体转换为响应DTO
     */
    private IchResourceResponse convertToResponse(IchResource resource) {
        return IchResourceResponse.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .description(resource.getDescription())
                .type(resource.getType())
                .contentUrl(resource.getContentUrl())
                .copyrightInfo(resource.getCopyrightInfo())
                .category(CategoryResponse.builder()
                        .id(resource.getCategory().getId())
                        .name(resource.getCategory().getName())
                        .description(resource.getCategory().getDescription())
                        .resourceCount(ichResourceRepository.countByCategoryId(resource.getCategory().getId()))
                        .build())
                .uploader(IchResourceResponse.UploaderInfo.builder()
                        .id(resource.getUploader().getId())
                        .username(resource.getUploader().getUsername())
                        .nickname(resource.getUploader().getNickname())
                        .avatar(resource.getUploader().getAvatar())
                        .build())
                .createdAt(resource.getCreatedAt())
                .updatedAt(resource.getUpdatedAt())
                .build();
    }

    /**
     * 将实体分页转换为响应DTO分页
     */
    private Page<IchResourceResponse> convertToResponsePage(Page<IchResource> resourcePage) {
        List<IchResourceResponse> responseList = resourcePage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(
                responseList,
                resourcePage.getPageable(),
                resourcePage.getTotalElements()
        );
    }
}