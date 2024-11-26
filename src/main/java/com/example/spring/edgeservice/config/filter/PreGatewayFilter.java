package com.example.spring.edgeservice.config.filter;

import com.example.spring.edgeservice.client.AuthServiceClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Order(0)
@Component
public class PreGatewayFilter extends AbstractGatewayFilterFactory<PreGatewayFilter.Config> {

    private final AuthServiceClient authServiceClient;

    public PreGatewayFilter(AuthServiceClient authServiceClient) {
        super(Config.class);
        this.authServiceClient = authServiceClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
            log.info("token: {}", token);

            // 토큰 유효성 검사
            if (token == null || !token.startsWith(config.getTokenPrefix())) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            if (authServiceClient.validToken(token) == 2) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.valueOf(config.getAuthenticationTimeout()));
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    @Getter
    @Setter
    public static class Config {
        private String tokenPrefix = "Bearer ";
        private int authenticationTimeout = 419;
    }


}

