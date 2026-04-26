package com.learning.consumerexample.Service;

import com.learning.consumerexample.dto.Customer;
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
//    @KafkaListener(topics = "testtopic", groupId = "test-group-1")
//    public void consume2(String message){
//        log.info("Consumer2 Consumed {}", message);
//    }
//    @KafkaListener(topics = "testtopic", groupId = "test-group-1")
//    public void consume3(String message){
//        log.info("Consumer3 Consumed {}", message);
//    }
//    @KafkaListener(topics = "testtopic", groupId = "test-group-1")
//    public void consume4(String message){
//        log.info("Consumer4 Consumed {}", message);
//    }

}
