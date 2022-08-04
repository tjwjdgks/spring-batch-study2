package com.example.springbatchbase.batch;

import com.example.springbatchbase.tasklet.ExecutionContextTasklet1;
import com.example.springbatchbase.tasklet.ExecutionContextTasklet2;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final JobExecutionListener jobRepositoryListener;

    private final ExecutionContextTasklet1 tasklet1;
    private final ExecutionContextTasklet2 tasklet2;
    @Bean
    public Job helloJob(){
        return jobBuilderFactory.get("helloJob")
            .start(helloStep1())
            .next(helloStep2())
            .next(step1())
            .next(step2())
            .listener(jobRepositoryListener)
            .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
            .tasklet(tasklet2)
            .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .tasklet(tasklet1)
            .build();
    }

    @Bean
    public Step helloStep1(){
        return stepBuilderFactory.get("helloStep1")
            .tasklet((stepContribution, chunkContext) -> {

                // StepContribution 으로 jobParameters 얻기 (실제 jobParameter 얻기)
                JobParameters jobParameters = stepContribution.getStepExecution().getJobExecution().getJobParameters();
                jobParameters.getString("name");
                jobParameters.getLong("seq");
                jobParameters.getDate("date");
                jobParameters.getDouble("age");

                // ChunkContext로 jobParameters 얻기 (jobParameter 기반으로 조회용)
                Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();


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
