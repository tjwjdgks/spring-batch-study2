package com.example.springbatchbase.runner;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //builder 패턴으로 생성 type은 string, date, long, double 지원
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("name", "user1")
            .addLong("seq",2L)
            .addDate("date", new Date())
            .addDouble("age",16.5)
            .toJobParameters();

        jobLauncher.run(job,jobParameters);
    }
}
