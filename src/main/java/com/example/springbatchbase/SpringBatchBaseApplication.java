package com.example.springbatchbase;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchBaseApplication.class, args);
    }

}
