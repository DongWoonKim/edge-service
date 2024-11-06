package com.example.spring.edgeservice.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter implements WebFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();
        log.info("Token filter path :: {}", requestPath);
        // 인증이 필요 없는 경로는 필터를 통과시킴
        if (
                "/refresh-token".equals(requestPath) ||
                        "/webs/login".equals(requestPath)
        ) {
            log.info("No Auth");
            return chain.filter(exchange);
        }

        // 토큰 추출
        String token = resolveToken(exchange.getRequest());
        log.info("Token filter token :: {}", token);
        if (token != null) {
            return Mono.just(token)
                    .flatMap(tokenProvider::validToken)
                    .flatMap(validationResult -> {
                        log.info("Token filter validationResult :: {}",  validationResult);
                        if (validationResult == 1) {
                            // 토큰이 유효할 경우
                            return tokenProvider.getAuthentication(token)  // Mono<Authentication>
                                    .flatMap(authentication -> {
                                        // Authentication을 SecurityContext에 저장
                                        return chain.filter(exchange)
                                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                                                .transformDeferred(call -> {
                                                    // web-front로 전달할 헤더 추가
                                                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                                            .header("X-User-Name", authentication.getName())
                                                            .header("X-User-Roles", authentication.getAuthorities().stream()
                                                                    .map(GrantedAuthority::getAuthority)
                                                                    .collect(Collectors.joining(",")))
                                                            .build();
                                                    return chain.filter(
                                                            exchange.mutate()
                                                                    .request(mutatedRequest)
                                                                    .build()
                                                    );
                                                });
                                    });
                        } else if (validationResult == 2) {
                            // 토큰이 만료된 경우
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        } else if (validationResult == 3) {
                            // 토큰이 유효하지 않은 경우
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }

                        // 추가적인 조건이 없다면 기본적으로 요청을 계속 진행
                        return chain.filter(exchange);
                    })
                    .onErrorResume(e -> {
                        log.error("Token validation error: ", e);
                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                        return exchange.getResponse().setComplete();
                    });
        }

        // 토큰이 없는 경우 계속 진행
        return chain.filter(exchange);
    }

    private String resolveToken(ServerHttpRequest request) {
        // Authorization 헤더에서 JWT 토큰 추출
        String bearerToken = request.getHeaders().getFirst(HEADER_AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
