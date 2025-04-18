package br.com.dealership.car.api.adapter.out.sqs.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Bean
    public SqsTemplate sqsTemplate(@Value("${aws.sqs.queue.name}") String queueName, @Value("${cloud.aws.sqs.region}") String region, @Value("${cloud.aws.sqs.endpoint}") String endpoint) {
        return SqsTemplate.builder()
                .sqsAsyncClient(SqsAsyncClient.builder()
                        .endpointOverride(URI.create(endpoint))
                        .region(Region.of(region))
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build())
                .configure(options -> options
                        .defaultQueue(queueName))
                .build();
    }

}