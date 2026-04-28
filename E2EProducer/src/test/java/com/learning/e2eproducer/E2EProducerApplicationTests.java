package com.learning.e2eproducer;

import com.learning.e2eproducer.dto.Customer;
import com.learning.e2eproducer.service.KafkaMessagePublisher;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.testcontainers.kafka.KafkaContainer;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E2EProducerApplicationTests {

    @Autowired
    private KafkaMessagePublisher publisher ;

    @Autowired
    private KafkaContainer kafkaContainer;

    @Test
    void testSendEventsToTopic() {
        Customer customer = new Customer(263, "test-user", "test@gmail.com", "987654321");
        try (Consumer<String, Customer> consumer = createConsumer()) {
            consumer.subscribe(java.util.List.of("testtopic-customer"));

            publisher.sendEventsToTopic(customer);

            ConsumerRecord<String, Customer> record =
                    KafkaTestUtils.getSingleRecord(consumer, "testtopic-customer");

            assertThat(record).isNotNull();
            assertThat(record.value()).isEqualTo(customer);
        }
    }

    private Consumer<String, Customer> createConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "e2e-producer-test-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JacksonJsonDeserializer<Customer> valueDeserializer = new JacksonJsonDeserializer<>(Customer.class);
        valueDeserializer.addTrustedPackages("*");

        return new org.apache.kafka.clients.consumer.KafkaConsumer<>(
                props,
                new StringDeserializer(),
                valueDeserializer
        );
    }

}
