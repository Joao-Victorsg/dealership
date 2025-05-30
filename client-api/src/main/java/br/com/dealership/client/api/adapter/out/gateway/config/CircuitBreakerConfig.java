package br.com.dealership.client.api.adapter.out.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CircuitBreakerConfig {

    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerConfig.class);
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void configureCircuitBreakerEventListeners() {
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(circuitBreaker -> circuitBreaker.getEventPublisher()
                        .onStateTransition(this::logStateTransition));
    }

    private void logStateTransition(CircuitBreakerOnStateTransitionEvent event) {
        var state = event.getStateTransition().getToState();
        var circuitBreakerName = event.getCircuitBreakerName();

        switch (state) {
            case OPEN -> logger.warn("CircuitBreaker '{}' transitioned to OPEN state", circuitBreakerName);
            case CLOSED -> logger.info("CircuitBreaker '{}' transitioned to CLOSED state", circuitBreakerName);
            case HALF_OPEN -> logger.info("CircuitBreaker '{}' transitioned to HALF_OPEN state", circuitBreakerName);
            default -> logger.debug("CircuitBreaker '{}' transitioned to {} state", circuitBreakerName, state);
        }
    }
}