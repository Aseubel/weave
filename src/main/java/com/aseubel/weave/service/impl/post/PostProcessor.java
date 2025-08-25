package com.aseubel.weave.service.impl.post;

import com.aseubel.weave.common.disruptor.Element;
import com.aseubel.weave.common.disruptor.EventType;
import com.aseubel.weave.common.repochain.Processor;
import com.aseubel.weave.common.repochain.ProcessorChain;
import com.aseubel.weave.common.repochain.Result;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.pojo.entity.post.PostLike;
import com.aseubel.weave.repository.PostLikeRepository;
import com.aseubel.weave.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Aseubel
 * @date 2025/7/13 上午10:55
 */
@Component
public class PostProcessor implements Processor<Element> {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Override
    public Result<Element> process(Element data, int index, ProcessorChain<Element> chain) {
        EventType eventType = data.getEventType();
        switch (eventType) {
            case POST_LIKE -> handlePostLike(data);
            case POST_UNLIKE -> handlePostUnlike(data);
            case POST_COMMENT -> handlePostComment(data);
            case POST_TOP -> handlePostTop(data);
        }
        chain.process(data, index + 1);
        return null;
    }

    private void handlePostLike(Element data) {
        PostLike postLike = getPostLike(data);
        if (!postLikeRepository.existsByUserAndPostAndType(postLike.getUser(), postLike.getPost(), postLike.getType())) {
            postLikeRepository.save(postLike);
        }
    }

    private void handlePostUnlike(Element data) {
        PostLike postLike = getPostLike(data);
        postLikeRepository.deleteByUserAndPost(postLike.getUser(), postLike.getPost());
    }

    private void handlePostComment(Element data) {

    }

    private void handlePostTop(Element data) {

    }

    private Post getPost(Element data) {
        return (Post) data.getData();
    }

    private PostLike getPostLike(Element data) {
        return (PostLike) data.getData();
    }
}
