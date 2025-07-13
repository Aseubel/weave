package com.aseubel.weave;

import com.aseubel.weave.common.disruptor.DisruptorProducer;
import com.aseubel.weave.common.disruptor.Element;
import com.aseubel.weave.common.disruptor.EventType;
import com.aseubel.weave.common.repochain.Processor;
import com.aseubel.weave.common.repochain.ProcessorChain;
import com.aseubel.weave.common.repochain.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025/7/13 上午11:05
 */
@SpringBootTest
public class DisruptorTest {

    @Autowired
    private DisruptorProducer disruptorProducer;
    @Resource
    private ProcessorChain<Element> disruptorChain;

    public static class TestHandler implements Processor<Element> {
        public static final List<Integer> list = new ArrayList<>();
        @Override
        public Result<Element> process(Element data, int index, ProcessorChain<Element> chain) {
            list.add((Integer) data.getData());
            return null;
        }
    }
    @Test
    public void multiThreadTest() {
        disruptorChain.clearProcessor();
        disruptorChain.addProcessor(new TestHandler());
        List<Integer> rightList = new ArrayList<>();
        for (int i=1;i<=100;i++) {
            rightList.add(i);
            disruptorProducer.publish(i, EventType.TEST);
        }
        Assertions.assertEquals(rightList, TestHandler.list);
    }
}
