package com.learning.producerexample.service;

import com.learning.producerexample.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisher {
    @Autowired
    private KafkaTemplate<String, Object> template ;

    public void sendMessageToTopic(String message){
        CompletableFuture<SendResult<String, Object>> future = template.send("testtopic-string", message);
        future.whenComplete((result, ex)->{
            if (ex == null) {
                System.out.println("Sent message=[" + result.toString() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        result.toString() + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendEventsToTopic(Customer customer){
        try {
            CompletableFuture<SendResult<String, Object>> future = template.send("testtopic-customer", customer);
            future.whenComplete((result, ex)->{
                if (ex == null) {
                    System.out.println("Sent message=[" + result.toString() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            result.toString() + "] due to : " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
