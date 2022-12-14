package com.example.springbatchbase.batch;

import com.example.springbatchbase.incrementer.CustomJobParametersIncrementer;
import com.example.springbatchbase.tasklet.ExecutionContextTasklet1;
import com.example.springbatchbase.tasklet.ExecutionContextTasklet2;
import com.example.springbatchbase.validator.CustomJobParametersValidator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
//            .incrementer(new CustomJobParametersIncrementer())
            .incrementer(new RunIdIncrementer())
            .validator(new DefaultJobParametersValidator(new String[]{"name","date"},new String[]{"seq","age"}))
//            .validator(new CustomJobParametersValidator())
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

                // StepContribution ?????? jobParameters ?????? (?????? jobParameter ??????)
                JobParameters jobParameters = stepContribution.getStepExecution().getJobExecution().getJobParameters();
                jobParameters.getString("name");
                jobParameters.getLong("seq");
                jobParameters.getDate("date");
                jobParameters.getDouble("age");

                // ChunkContext??? jobParameters ?????? (jobParameter ???????????? ?????????)
                Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();


                System.out.println("hello spring batch1");
               // null ????????? task ?????? ?????? ??? ??????
               return RepeatStatus.FINISHED; // ????????? ?????? ?????? ??? ?????? null ??? ??????
            }).build();
    }

    @Bean
    public Step helloStep2(){
        return stepBuilderFactory.get("helloStep2")
            .tasklet((stepContribution, chunkContext) -> {
                System.out.println("hello spring batch2");
                // null ????????? task ?????? ?????? ??? ??????
                return RepeatStatus.FINISHED; // ????????? ?????? ?????? ??? ?????? null ??? ??????
            }).build();
    }


}
