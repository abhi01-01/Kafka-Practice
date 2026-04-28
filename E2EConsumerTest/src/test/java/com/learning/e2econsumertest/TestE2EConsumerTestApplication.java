package com.learning.e2econsumertest;

import org.springframework.boot.SpringApplication;

public class TestE2EConsumerTestApplication {

    public static void main(String[] args) {
        SpringApplication.from(E2EConsumerTestApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
