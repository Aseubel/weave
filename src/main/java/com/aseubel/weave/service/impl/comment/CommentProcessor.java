package com.aseubel.weave.service.impl.comment;

import com.aseubel.weave.common.disruptor.Element;
import com.aseubel.weave.common.disruptor.EventType;
import com.aseubel.weave.common.repochain.Processor;
import com.aseubel.weave.common.repochain.ProcessorChain;
import com.aseubel.weave.common.repochain.Result;
import com.aseubel.weave.pojo.entity.comment.Comment;
import com.aseubel.weave.pojo.entity.comment.CommentLike;
import com.aseubel.weave.repository.CommentLikeRepository;
import com.aseubel.weave.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @date 2025/7/13 上午10:55
 */
@Component
public class CommentProcessor implements Processor<Element> {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Override
    public Result<Element> process(Element data, int index, ProcessorChain<Element> chain) {
        EventType eventType = data.getEventType();
        switch (eventType) {
            case COMMENT_LIKE -> handleCommentLike(data);
            case COMMENT_UNLIKE -> handleCommentUnlike(data);
        }
        chain.process(data, index + 1);
        return null;
    }

    private void handleCommentLike(Element data) {
        CommentLike commentLike = getCommentLike(data);
        commentLikeRepository.save(commentLike);
        commentRepository.updateLikeCount(commentLike.getComment().getId(), 1L);
    }

    private void handleCommentUnlike(Element data) {
        CommentLike commentLike = getCommentLike(data);
        commentLikeRepository.delete(commentLike);
    }

    private Comment getComment(Element data) {
        return (Comment) data.getData();
    }

    private CommentLike getCommentLike(Element data) {
        return (CommentLike) data.getData();
    }
}
