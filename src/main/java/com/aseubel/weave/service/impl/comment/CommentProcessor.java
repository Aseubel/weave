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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

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
    @Autowired
    private PlatformTransactionManager transactionManager;

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
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            CommentLike commentLike = getCommentLike(data);
            if (commentLikeRepository.findByUserAndComment(commentLike.getUser(), commentLike.getComment()).isEmpty()){
                commentLikeRepository.save(commentLike);
            }
            return null;
        });
    }

    private void handleCommentUnlike(Element data) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            CommentLike commentLike = getCommentLike(data);
            commentLikeRepository.deleteByUserAndComment(commentLike.getUser(), commentLike.getComment());
            return null;
        });
    }

    private Comment getComment(Element data) {
        return (Comment) data.getData();
    }

    private CommentLike getCommentLike(Element data) {
        return (CommentLike) data.getData();
    }
}
