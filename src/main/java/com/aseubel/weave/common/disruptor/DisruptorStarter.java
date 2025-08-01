package com.aseubel.weave.common.disruptor;

import com.aseubel.weave.common.repochain.ProcessorChain;
import com.lmax.disruptor.EventHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @date 2025/5/7 下午4:38
 */
@Slf4j
@Component
public class DisruptorStarter implements EventHandler<Element> {

    @Resource
    private ProcessorChain<Element> disruptorChain;

    @Override
    public void onEvent(Element element, long l, boolean b) throws Exception {
//        log.info("接收到disruptor事件：{}", element.getEventType());
        disruptorChain.process(element);
    }
}
