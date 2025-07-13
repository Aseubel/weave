package com.aseubel.weave.task.post;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static com.aseubel.weave.redis.LuaScript.HASH_GET_REMOVE_SCRIPT;

/**
 * @author Aseubel
 * @date 2025/7/11 下午10:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncLike {

    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;

    // 每分钟同步一次点赞数
    @Scheduled(fixedRate = 60000)
    public void syncPostLike() {
        String key = KeyBuilder.postLikeRecentKey();
        Map<Long, Long> postSet = (Map<Long, Long>) redisTemplate.execute(HASH_GET_REMOVE_SCRIPT, Collections.singletonList(key));
        if (CollectionUtil.isNotEmpty(postSet)) {
            for (Map.Entry<Long, Long> entry : postSet.entrySet()) {
                Long postId = entry.getKey();
                Long likeCount = entry.getValue();
                postRepository.updateLikeCount(postId, likeCount);
            }
            log.info("Sync post like count success, count: {}", postSet.size());
        }
    }

}
