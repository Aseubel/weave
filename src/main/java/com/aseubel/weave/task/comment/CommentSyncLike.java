package com.aseubel.weave.task.comment;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.CommentRepository;
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
 * @date 2025/7/13 下午12:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommentSyncLike {

    private final CommentRepository commentRepository;
    private final StringRedisTemplate redisTemplate;

    // 每分钟同步一次点赞数
    @Scheduled(fixedRate = 60000)
    public void syncCommentLike() {
        String key = KeyBuilder.commentLikeRecentKey();
        Map<Long, Long> commentSet = (Map<Long, Long>) redisTemplate.execute(HASH_GET_REMOVE_SCRIPT, Collections.singletonList(key));
        if (CollectionUtil.isNotEmpty(commentSet)) {
            for (Map.Entry<Long, Long> entry : commentSet.entrySet()) {
                Long commentId = entry.getKey();
                Long likeCount = entry.getValue();
                commentRepository.updateLikeCount(commentId, likeCount);
            }
            log.info("Sync comment like count success, count: {}", commentSet.size());
        }
    }

}
