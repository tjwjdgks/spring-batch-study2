package com.example.springbatchbase.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob(){
        return jobBuilderFactory.get("helloJob")
            .start(helloStep1())
            .next(helloStep2())
            .build();
    }

    @Bean
    public Step helloStep1(){
        return stepBuilderFactory.get("helloStep1")
            .tasklet((stepContribution, chunkContext) -> {
               System.out.println("hello spring batch1");
               // null 일경우 task 한번 실행 후 종료
               return RepeatStatus.FINISHED; // 기능이 한번 실행 후 종료 null 과 같음
            }).build();
    }

    @Bean
    public Step helloStep2(){
        return stepBuilderFactory.get("helloStep2")
            .tasklet((stepContribution, chunkContext) -> {
                System.out.println("hello spring batch2");
                // null 일경우 task 한번 실행 후 종료
                return RepeatStatus.FINISHED; // 기능이 한번 실행 후 종료 null 과 같음
            }).build();
    }


}
