package com.ml.hotel_ml_apigateway_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic registerTopic(){
        return TopicBuilder.name("register_topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic loginTopic(){
        return TopicBuilder.name("login_topic")
                .partitions(10)
                .replicas(1)
                .build();
    }


}
