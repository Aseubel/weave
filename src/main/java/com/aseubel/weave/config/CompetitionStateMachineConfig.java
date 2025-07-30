package com.aseubel.weave.config;


import com.aseubel.weave.pojo.entity.competition.CompetitionEvent;
import com.aseubel.weave.pojo.entity.competition.CompetitionState;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aseubel
 * @date 2025/7/29
 */
@Configuration
public class CompetitionStateMachineConfig {

    private static final String STATE_MACHINE_ID = "competitionStateMachine";

    @Bean
    public StateMachine<CompetitionState, CompetitionEvent, Void> competitionStateMachine() {
        StateMachineBuilder<CompetitionState, CompetitionEvent, Void> builder = StateMachineBuilderFactory.create();

        // 开始比赛：未开始 -> 公开投票信息
        builder.externalTransition()
                .from(CompetitionState.NOT_STARTED)
                .to(CompetitionState.PUBLIC_RUNNING)
                .on(CompetitionEvent.START)
                .perform((from, to, event, context) -> System.out.println("Transitioning from " + from + " to " + to + " on " + event));

        // 公开投票：已开始（隐藏投票信息） -> 已开始（公开投票信息）
        builder.externalTransition()
                .from(CompetitionState.HIDDEN_RUNNING)
                .to(CompetitionState.PUBLIC_RUNNING)
                .on(CompetitionEvent.PUBLIC_VOTING)
                .perform((from, to, event, context) -> System.out.println("Transitioning from " + from + " to " + to + " on " + event));

        // 隐藏投票：已开始（公开投票信息） -> 已开始（隐藏投票信息）
        builder.externalTransition()
                .from(CompetitionState.PUBLIC_RUNNING)
                .to(CompetitionState.HIDDEN_RUNNING)
                .on(CompetitionEvent.HIDE_VOTING)
                .perform((from, to, event, context) -> System.out.println("Transitioning from " + from + " to " + to + " on " + event));

        // 终止比赛：已开始（公开投票信息） -> 已结束
        builder.externalTransition()
                .from(CompetitionState.PUBLIC_RUNNING)
                .to(CompetitionState.ENDED)
                .on(CompetitionEvent.END)
                .perform((from, to, event, context) -> System.out.println("Transitioning from " + from + " to " + to + " on " + event));

        // 终止比赛：已开始（隐藏投票信息） -> 已结束
        builder.externalTransition()
                .from(CompetitionState.HIDDEN_RUNNING)
                .to(CompetitionState.ENDED)
                .on(CompetitionEvent.END)
                .perform((from, to, event, context) -> System.out.println("Transitioning from " + from + " to " + to + " on " + event));


        return builder.build(STATE_MACHINE_ID);
    }
}
