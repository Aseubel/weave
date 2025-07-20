package com.aseubel.weave.disruptor;

import com.aseubel.weave.common.disruptor.Element;
import com.aseubel.weave.common.repochain.ProcessorChain;
import com.aseubel.weave.service.impl.comment.CommentProcessor;
import com.aseubel.weave.service.impl.post.PostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aseubel
 * @date 2025/5/7 下午4:44
 */
@Configuration
public class DisruptorChainConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "disruptorChain")
    public ProcessorChain<Element> disruptorChain() {
        ProcessorChain<Element> processorChain = new ProcessorChain<>();
        processorChain.addProcessor(applicationContext.getBean(PostProcessor.class));
        processorChain.addProcessor(applicationContext.getBean(CommentProcessor.class));
        return processorChain;
    }
}
