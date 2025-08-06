package com.aseubel.weave.task.post;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.redis.IRedisService;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.aseubel.weave.redis.LuaScript.HASH_GET_REMOVE_SCRIPT;


/**
 * @author Aseubel
 * @date 2025/7/11 下午10:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PostSyncLike {

    private final PostRepository postRepository;
    private final IRedisService redisService;

    // 每分钟同步一次点赞数
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void syncPostLike() {
        String key = KeyBuilder.postLikeCountKey();
        if (!redisService.isExists(key)) {
            return;
        }
        List<Object> postList = redisService.execute(
                HASH_GET_REMOVE_SCRIPT, RScript.ReturnType.MULTI, Collections.singletonList(key), 0);
        if (CollectionUtil.isNotEmpty(postList)) {
            // 从 List 手动构建 Map
            Map<String, String> postMap = new HashMap<>();
            // HGETALL 返回的 List 是 [key1, value1, key2, value2, ...] 的形式，所以步长为 2
            for (int i = 0; i < postList.size(); i += 2) {
                postMap.put(String.valueOf(postList.get(i)), String.valueOf(postList.get(i + 1)));
            }
            Set<Long> postIdsToUpdate = postMap.keySet().stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            // 拿到所有需要更新的 Post
            List<Post> postsToUpdate = postRepository.findAllById(postIdsToUpdate);
            for (Post post : postsToUpdate) {
                Long newLikeCount = Long.parseLong(postMap.get(String.valueOf(post.getId()))) + post.getLikeCount();
                post.setLikeCount(newLikeCount);
            }
            if (CollectionUtil.isNotEmpty(postsToUpdate)) {
                postRepository.saveAll(postsToUpdate);
                log.info("Sync post like count success, count: {}", postMap.size());
            }
        }
    }

}
