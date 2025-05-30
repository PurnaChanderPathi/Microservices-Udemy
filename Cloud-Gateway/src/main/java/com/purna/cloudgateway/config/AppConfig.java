package com.purna.cloudgateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class AppConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> deafultCustomizer(){
        return factory -> factory.configureDefault(
                id -> new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(
                                CircuitBreakerConfig.ofDefaults()
                        ).build()
        );
    }

    @Bean
    KeyResolver userKeySolver() {
        return exchange -> Mono.just("userKey");
    }
}
