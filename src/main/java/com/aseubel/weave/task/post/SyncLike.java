package com.aseubel.weave.task.post;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.weave.pojo.entity.Post;
import com.aseubel.weave.pojo.entity.PostLike;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.PostLikeRepository;
import com.aseubel.weave.repository.PostRepository;
import com.aseubel.weave.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.aseubel.weave.redis.LuaScript.SET_GET_REMOVE_SCRIPT;

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

    @PostConstruct
    public void syncPostLike() {
        String key = KeyBuilder.postLikeRecentKey();
        Set<Long> postSet = (Set<Long>) redisTemplate.execute(SET_GET_REMOVE_SCRIPT, Collections.singletonList(key));
        if (CollectionUtil.isNotEmpty(postSet)) {
            postSet.forEach(postId -> {
                Long likeCount = Long.valueOf(redisTemplate.opsForValue().get(KeyBuilder.postLikeCountKey(postId)));
                postRepository.save(getPostWithLikeCount(postId, likeCount));
            });
        }
    }

    private Post getPostWithLikeCount(Long postId, Long likeCount) {
        Post post = new Post();
        post.setId(postId);
        post.setLikeCount(likeCount);
        return post;
    }
}
