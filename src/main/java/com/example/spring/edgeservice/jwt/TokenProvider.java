package com.example.spring.edgeservice.jwt;

import com.example.spring.edgeservice.auth.AuthClient;
import com.example.spring.edgeservice.dto.AuthenticationRequestDTO;
import com.example.spring.edgeservice.dto.AuthenticationResponseDTO;
import com.example.spring.edgeservice.dto.ValidTokenRequestDTO;
import com.example.spring.edgeservice.dto.ValidTokenResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final AuthClient authClient;

    public Mono<Integer> validToken(String token) {
        return Mono.fromCallable(() -> {
                    ValidTokenRequestDTO requestDTO = ValidTokenRequestDTO.builder()
                            .refreshToken(token)
                            .build();
                    ValidTokenResponseDTO response = authClient.validToken(requestDTO);
                    return response.getStatusNum(); // auth-service의 응답 결과를 반환
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(FeignException.class, e -> {
                    log.error("Auth service communication error: {}", e.getMessage());
                    return Mono.just(3);  // 서비스 통신 실패시 유효하지 않은 토큰으로 처리
                });
    }

    public Mono<Authentication> getAuthentication(String token) {
        return Mono.fromCallable(() -> {
                    AuthenticationRequestDTO requestDTO = AuthenticationRequestDTO.builder()
                            .token(token)
                            .build();
                    AuthenticationResponseDTO response = authClient.getAuthentication(requestDTO);
                    return response.getAuthentication(); // Authentication 객체 직접 반환
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(FeignException.class, e -> {
                    log.error("Auth service authentication error: {}", e.getMessage());
                    return Mono.empty(); // 또는 적절한 기본값/에러 처리
                });
    }
}
