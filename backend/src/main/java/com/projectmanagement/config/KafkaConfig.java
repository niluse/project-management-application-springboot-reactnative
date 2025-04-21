package com.projectmanagement.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic taskCreatedTopic() {
        return TopicBuilder.name("task-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic taskUpdatedTopic() {
        return TopicBuilder.name("task-updated")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic projectCreatedTopic() {
        return TopicBuilder.name("project-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic projectUpdatedTopic() {
        return TopicBuilder.name("project-updated")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic aiEstimationRequestTopic() {
        return TopicBuilder.name("ai-estimation-request")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic aiEstimationResponseTopic() {
        return TopicBuilder.name("ai-estimation-response")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic jiraUpdateTopic() {
        return TopicBuilder.name("jira-update")
                .partitions(3)
                .replicas(1)
                .build();
    }
} 