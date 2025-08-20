package com.aseubel.weave.service.impl;

import com.aseubel.weave.common.exception.BusinessException;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.user.UserResponse;
import com.aseubel.weave.pojo.entity.Follow;
import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.repository.FollowRepository;
import com.aseubel.weave.repository.UserRepository;
import com.aseubel.weave.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 关注服务实现类
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

        private final FollowRepository followRepository;
        private final UserRepository userRepository;

        @Override
        @Transactional
        public void followUser(Long userId) {
                User currentUser = UserContext.getCurrentUser();

                if (currentUser.getId().equals(userId)) {
                        throw new BusinessException("不能关注自己");
                }

                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                // 检查是否已经关注
                Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(currentUser, targetUser);

                if (existingFollow.isPresent()) {
                        Follow follow = existingFollow.get();
                        if (follow.getStatus() == Follow.FollowStatus.ACTIVE) {
                                throw new BusinessException("已经关注了该用户");
                        } else {
                                // 重新激活关注
                                follow.setStatus(Follow.FollowStatus.ACTIVE);
                                followRepository.save(follow);
                        }
                } else {
                        // 创建新的关注关系
                        Follow follow = Follow.builder()
                                        .follower(currentUser)
                                        .following(targetUser)
                                        .status(Follow.FollowStatus.ACTIVE)
                                        .build();
                        followRepository.save(follow);
                }
        }

        @Override
        @Transactional
        public void unfollowUser(Long userId) {
                User currentUser = UserContext.getCurrentUser();
                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                Follow follow = followRepository.findByFollowerAndFollowing(currentUser, targetUser)
                                .orElseThrow(() -> new BusinessException("未关注该用户"));

                followRepository.delete(follow);
        }

        @Override
        public boolean isFollowing(Long userId) {
                User currentUser = UserContext.getCurrentUser();
                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                return followRepository.existsByFollowerAndFollowingAndStatus(
                                currentUser, targetUser, Follow.FollowStatus.ACTIVE);
        }

        @Override
        public PageResponse<UserResponse> getFollowing(Pageable pageable) {
                User currentUser = UserContext.getCurrentUser();

                Page<Follow> follows = followRepository.findByFollowerAndStatusOrderByCreatedAtDesc(
                                currentUser, Follow.FollowStatus.ACTIVE, pageable);

                List<UserResponse> responses = follows.getContent().stream()
                                .map(follow -> convertToUserResponse(follow.getFollowing()))
                                .collect(Collectors.toList());

                return PageResponse.<UserResponse>builder()
                                .content(responses)
                                .total(follows.getTotalElements())
                                .totalPages(follows.getTotalPages())
                                .size(follows.getSize())
                                .page(follows.getNumber() + 1)
                                .isFirst(follows.isFirst())
                                .isLast(follows.isLast())
                                .hasNext(follows.hasNext())
                                .hasPrevious(follows.hasPrevious())
                                .build();
        }

        @Override
        public PageResponse<UserResponse> getUserFollowing(Long userId, Pageable pageable) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                Page<Follow> follows = followRepository.findByFollowerAndStatusOrderByCreatedAtDesc(
                                user, Follow.FollowStatus.ACTIVE, pageable);

                List<UserResponse> responses = follows.getContent().stream()
                                .map(follow -> convertToUserResponse(follow.getFollowing()))
                                .collect(Collectors.toList());

                return PageResponse.<UserResponse>builder()
                                .content(responses)
                                .total(follows.getTotalElements())
                                .totalPages(follows.getTotalPages())
                                .size(follows.getSize())
                                .page(follows.getNumber() + 1)
                                .isFirst(follows.isFirst())
                                .isLast(follows.isLast())
                                .hasNext(follows.hasNext())
                                .hasPrevious(follows.hasPrevious())
                                .build();
        }

        @Override
        public PageResponse<UserResponse> getFollowers(Pageable pageable) {
                User currentUser = UserContext.getCurrentUser();

                Page<Follow> follows = followRepository.findByFollowingAndStatusOrderByCreatedAtDesc(
                                currentUser, Follow.FollowStatus.ACTIVE, pageable);

                List<UserResponse> responses = follows.getContent().stream()
                                .map(follow -> convertToUserResponse(follow.getFollower()))
                                .collect(Collectors.toList());

                return PageResponse.<UserResponse>builder()
                                .content(responses)
                                .total(follows.getTotalElements())
                                .totalPages(follows.getTotalPages())
                                .size(follows.getSize())
                                .page(follows.getNumber() + 1)
                                .isFirst(follows.isFirst())
                                .isLast(follows.isLast())
                                .hasNext(follows.hasNext())
                                .hasPrevious(follows.hasPrevious())
                                .build();
        }

        @Override
        public PageResponse<UserResponse> getUserFollowers(Long userId, Pageable pageable) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                Page<Follow> follows = followRepository.findByFollowingAndStatusOrderByCreatedAtDesc(
                                user, Follow.FollowStatus.ACTIVE, pageable);

                List<UserResponse> responses = follows.getContent().stream()
                                .map(follow -> convertToUserResponse(follow.getFollower()))
                                .collect(Collectors.toList());

                return PageResponse.<UserResponse>builder()
                                .content(responses)
                                .total(follows.getTotalElements())
                                .totalPages(follows.getTotalPages())
                                .size(follows.getSize())
                                .page(follows.getNumber() + 1)
                                .isFirst(follows.isFirst())
                                .isLast(follows.isLast())
                                .hasNext(follows.hasNext())
                                .hasPrevious(follows.hasPrevious())
                                .build();
        }

        @Override
        public PageResponse<UserResponse> getMutualFollows(Pageable pageable) {
                User currentUser = UserContext.getCurrentUser();

                List<User> mutualFollows = followRepository.findMutualFollows(currentUser, Follow.FollowStatus.ACTIVE);

                // 简单分页处理
                int start = (int) pageable.getOffset();
                int end = Math.min(start + pageable.getPageSize(), mutualFollows.size());
                List<User> pageContent = mutualFollows.subList(start, end);

                List<UserResponse> responses = pageContent.stream()
                                .map(this::convertToUserResponse)
                                .collect(Collectors.toList());

                return PageResponse.<UserResponse>builder()
                                .content(responses)
                                .total((long) mutualFollows.size())
                                .totalPages((int) Math.ceil((double) mutualFollows.size() / pageable.getPageSize()))
                                .size(pageContent.size())
                                .page(pageable.getPageNumber() + 1)
                                .isFirst(pageable.getPageNumber() == 0)
                                .isLast(end >= mutualFollows.size())
                                .hasNext(end < mutualFollows.size())
                                .hasPrevious(pageable.getPageNumber() > 0)
                                .build();
        }

        @Override
        public long getFollowingCount() {
                User currentUser = UserContext.getCurrentUser();
                return followRepository.countByFollowerAndStatus(currentUser, Follow.FollowStatus.ACTIVE);
        }

        @Override
        public long getUserFollowingCount(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                return followRepository.countByFollowerAndStatus(user, Follow.FollowStatus.ACTIVE);
        }

        @Override
        public long getFollowersCount() {
                User currentUser = UserContext.getCurrentUser();
                return followRepository.countByFollowingAndStatus(currentUser, Follow.FollowStatus.ACTIVE);
        }

        @Override
        public long getUserFollowersCount(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                return followRepository.countByFollowingAndStatus(user, Follow.FollowStatus.ACTIVE);
        }

        @Override
        @Transactional
        public void blockUser(Long userId) {
                User currentUser = UserContext.getCurrentUser();
                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(currentUser, targetUser);

                if (existingFollow.isPresent()) {
                        Follow follow = existingFollow.get();
                        follow.setStatus(Follow.FollowStatus.BLOCKED);
                        followRepository.save(follow);
                } else {
                        // 创建屏蔽关系
                        Follow follow = Follow.builder()
                                        .follower(currentUser)
                                        .following(targetUser)
                                        .status(Follow.FollowStatus.BLOCKED)
                                        .build();
                        followRepository.save(follow);
                }
        }

        @Override
        @Transactional
        public void unblockUser(Long userId) {
                User currentUser = UserContext.getCurrentUser();
                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new BusinessException("用户不存在"));

                Follow follow = followRepository.findByFollowerAndFollowing(currentUser, targetUser)
                                .orElseThrow(() -> new BusinessException("未屏蔽该用户"));

                if (follow.getStatus() == Follow.FollowStatus.BLOCKED) {
                        followRepository.delete(follow);
                } else {
                        throw new BusinessException("该用户未被屏蔽");
                }
        }

        private UserResponse convertToUserResponse(User user) {
                User currentUser = UserContext.getCurrentUser();

                // 检查关注状态
                boolean isFollowing = followRepository.existsByFollowerAndFollowingAndStatus(
                                currentUser, user, Follow.FollowStatus.ACTIVE);
                boolean isFollowedBy = followRepository.existsByFollowerAndFollowingAndStatus(
                                user, currentUser, Follow.FollowStatus.ACTIVE);

                return UserResponse.builder()
                                .id(String.valueOf(user.getId()))
                                .username(user.getUsername())
                                .nickname(user.getNickname())
                                .avatar(Optional.ofNullable(user.getAvatar()).map(Image::getImageUrl).orElse(null))
                                .email(user.getEmail())
                                .phone(user.getMobile())
                                .bio(user.getBio())
                                .gender(user.getGender())
                                .birthday(user.getBirthday())
                                .createdAt(user.getCreatedAt())
                                .updatedAt(user.getUpdatedAt())
                                .followingCount(followRepository.countByFollowerAndStatus(user,
                                                Follow.FollowStatus.ACTIVE))
                                .followersCount(followRepository.countByFollowingAndStatus(user,
                                                Follow.FollowStatus.ACTIVE))
                                .postsCount(0L) // 需要从PostRepository获取
                                .isFollowing(isFollowing)
                                .isFollowedBy(isFollowedBy)
                                .isMutualFollow(isFollowing && isFollowedBy)
                                .build();
        }
}