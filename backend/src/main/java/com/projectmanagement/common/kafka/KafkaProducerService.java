package com.projectmanagement.common.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public <T> void sendMessage(String topic, String key, T message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            log.info("Sending Kafka message to topic: {}, key: {}, payload: {}", topic, key, payload);
            kafkaTemplate.send(topic, key, payload);
        } catch (JsonProcessingException e) {
            log.error("Error serializing message to JSON: {}", e.getMessage());
            throw new RuntimeException("Error serializing message", e);
        }
    }
} 