package com.learning.producerexample.controller;

import com.learning.producerexample.dto.Customer;
import com.learning.producerexample.service.KafkaMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer-app")
public class EventController {

    @Autowired
    private KafkaMessagePublisher publisher ;

    @GetMapping("/publish/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable String message){
        try {
            for(int i = 0 ; i < 10000 ; ++i){
                publisher.sendMessageToTopic(message) ;
            }
            return ResponseEntity.ok("Message Published successfully...") ;
        }catch (Exception e){
            return ResponseEntity.status(HttpStatusCode.valueOf(503)).build() ;
        }
    }

    @PostMapping("/publish")
    public void sendEvents(@RequestBody Customer customer){
        publisher.sendEventsToTopic(customer);
    }
}
