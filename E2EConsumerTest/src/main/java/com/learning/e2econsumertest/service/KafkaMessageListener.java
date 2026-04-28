package com.learning.e2econsumertest.service;

import com.learning.e2econsumertest.dto.Customer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageListener {
    Logger log = (Logger) LoggerFactory.getLogger(KafkaMessageListener.class);

    // Multiple consumers are implemented using Concurrency in real systems

    @KafkaListener(topics = "testtopic-customer", groupId = "test-group-1")
    public void consume1(Customer customer){
        log.info("Consumer1 Consumed {}", customer.toString());
    }
}
