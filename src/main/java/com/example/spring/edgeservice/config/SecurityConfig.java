package com.example.spring.edgeservice.config;

import com.example.spring.edgeservice.jwt.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ACCESS_DENIED_URL = "/access-denied";

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        exchange -> exchange
                                .pathMatchers("/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                                .pathMatchers(
                                "/webs",
                                "/webs/hello",
                                "/webs/login",
                                "/webs/join",
                                "/books",
                                "/books/**",
                                "/orders",
                                "/orders/**",
                                "/login",
                                "/logout",
                                "/refresh-token",
                                "/access-denied",
                                "/actuator/**",          // actuator 모든 엔드포인트 허용
                                "/actuator/health/**"    // 명시적으로 health 엔드포인트 허용
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(tokenAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((exchange, e) -> handleAccessDenied(exchange))
                        .authenticationEntryPoint((exchange, e) -> handleUnauthorized(exchange))
                )
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // 기본 인증 방식도 비활성화
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .build();
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
        exchange.getResponse().getHeaders().setLocation(URI.create(ACCESS_DENIED_URL));
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> handleAccessDenied(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
        exchange.getResponse().getHeaders().setLocation(URI.create(ACCESS_DENIED_URL));
        return exchange.getResponse().setComplete();
    }

}
