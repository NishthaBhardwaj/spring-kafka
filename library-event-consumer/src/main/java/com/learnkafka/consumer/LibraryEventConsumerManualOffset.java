package com.learnkafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.service.LibraryEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LibraryEventConsumerManualOffset implements AcknowledgingMessageListener<Integer, String> {

    @Autowired
    private LibraryEventService libraryEventService;

    @KafkaListener(topics = {"library-event"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Consumer message: {}", consumerRecord);
        try {
            libraryEventService.processLibraryEvent(consumerRecord);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            log.info("Error while processing the record: {}", e);
        }



    }
}
