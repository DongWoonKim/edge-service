package com.example.spring.edgeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        exchange -> exchange.pathMatchers(
                                "/css/**",   // 정적 리소스 경로 허용
                                "/js/**",    // 추가적인 정적 리소스 경로
                                "/static/**",
                                "/webs/hello",
                                "/webs/login",
                                "/webs/join",
                                "/books",
                                "/books/**",
                                "/orders",
                                "/orders/**",
                                "/actuator/**",          // actuator 모든 엔드포인트 허용
                                "/actuator/health/**"    // 명시적으로 health 엔드포인트 허용
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // 기본 인증 방식도 비활성화
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .build();
    }

}
