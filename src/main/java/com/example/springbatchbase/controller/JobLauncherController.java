package com.example.springbatchbase.controller;

import com.example.springbatchbase.dto.Member;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobLauncherController {

    private final Job job;


    private final BasicBatchConfigurer basicBatchConfigurer;
    @PostMapping("/batch")
    public String launch(@RequestBody Member member)
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("batch start");
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("id", member.getId())
            .addDate("date", new Date())
            .toJobParameters();
        // DI 받는 jobLauncher는 spring 에서 proxy 객체로 반환한다
        // basicBatchConfigurer는 Launcher 프록시 객체가 아니라 실제 Launcher를 가지고 있다
        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.run(job, jobParameters);
        log.info("batch end");
        return "batch completed";
    }
}
