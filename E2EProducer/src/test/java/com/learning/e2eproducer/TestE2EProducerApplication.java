package com.learning.e2eproducer;

import org.springframework.boot.SpringApplication;

public class TestE2EProducerApplication {

    public static void main(String[] args) {
        SpringApplication.from(E2EProducerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
