package com.example.spring.edgeservice.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter /*implements WebFilter*/ {

//    private final TokenProvider tokenProvider;
//    private final static String HEADER_AUTHORIZATION = "Authorization";
//    private final static String TOKEN_PREFIX = "Bearer ";
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        String requestPath = exchange.getRequest().getPath().value();
//        log.info("Token filter path :: {}", requestPath);
//
//        // 인증이 필요 없는 경로는 필터를 통과시킴
//        if (
//                "/refresh-token".equals(requestPath) ||
//                        "/login".equals(requestPath)
//        ) {
//            log.info("No need auth");
//            return chain.filter(exchange);
//        }
//
//        // 토큰 추출
//        String token = resolveToken(exchange.getRequest());
////        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJnYXVyaTc4OTFAZ21haWwuY29tIiwiaWF0IjoxNzMwODY0MTk3LCJleHAiOjE3MzEwMzY5OTcsInN1YiI6InRlc3QiLCJpZCI6MSwicm9sZSI6IlJPTEVfVVNFUiIsInVzZXJOYW1lIjoidGVzdCJ9.TDcCXG7br5bAbwS2WnM0sO00d4Yt2iio6DFIq2rx0nxp3YVW-Qbgv5mP98OpnIootdxkaIcb0sAFlhFVMtzQ4Q";
//        log.info("Token filter token :: {}", token);
//        if (token != null) {
//            return Mono.just(token)
//                    .flatMap(tokenProvider::validToken)
//                    .flatMap(validationResult -> {
//                        log.info("Token filter validationResult :: {}",  validationResult);
//                        if (validationResult == 1) {
//                            // 토큰이 유효할 경우
//                            return tokenProvider.getAuthentication(token)  // Mono<Authentication>
//                                    .flatMap(authentication -> {
//                                        // Authentication을 SecurityContext에 저장
//                                        return chain.filter(exchange)
//                                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
//                                                .transformDeferred(call -> {
//                                                    // web-front로 전달할 헤더 추가
//                                                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
//                                                            .header("X-User-Name", authentication.getName())
//                                                            .header("X-User-Roles", authentication.getAuthorities().stream()
//                                                                    .map(GrantedAuthority::getAuthority)
//                                                                    .collect(Collectors.joining(",")))
//                                                            .build();
//                                                    return chain.filter(
//                                                            exchange.mutate()
//                                                                    .request(mutatedRequest)
//                                                                    .build()
//                                                    );
//                                                });
//                                    });
//                        } else if (validationResult == 2) {
//                            // 토큰이 만료된 경우
//                            System.out.println("validationResult :: 2");
//                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                            return exchange.getResponse().setComplete();
//                        } else if (validationResult == 3) {
//                            if (!exchange.getRequest().getCookies().containsKey("redirected")) {
//                                exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
//                                exchange.getResponse().getHeaders().setLocation(URI.create("/webs/login"));
//
//                                // 리다이렉트 플래그 설정
//                                ResponseCookie redirectCookie = ResponseCookie.from("redirected", "true")
//                                        .path("/")
//                                        .maxAge(Duration.ofSeconds(3)) // 특정 시간 동안만 유지
//                                        .build();
//                                exchange.getResponse().addCookie(redirectCookie);
//
//                                return exchange.getResponse().setComplete();
//                            }
//                        }
//
//                        // 추가적인 조건이 없다면 기본적으로 요청을 계속 진행
//                        return chain.filter(exchange);
//                    })
//                    .onErrorResume(e -> {
//                        log.error("Token validation error: ", e);
//                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//                        return exchange.getResponse().setComplete();
//                    });
//        } else {
//            String refreshToken = getTokenFromCookiesOrRequest(exchange.getRequest());
//            // [로직1] refresh token이 있으면 access token을 auth-service에 요청한다.
//            log.info("Refresh token :: {}", refreshToken);
//
//            if (refreshToken != null) {
//                // 리프레시 토큰이 있는 경우 계속 진행
//                // 해당 페이지에서 리프레시 토큰으로 유효성 체크후 적합하지 않은경우 /login 페이지로 보낸다.
//                return chain.filter(exchange);
//            } else {
//                // access token & refresh token 모두 없는 경우
//                if (!exchange.getRequest().getCookies().containsKey("redirected")) {
//                    exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
//                    exchange.getResponse().getHeaders().setLocation(URI.create("/webs/login"));
//
//                    // 리다이렉트 플래그 설정
//                    ResponseCookie redirectCookie = ResponseCookie.from("redirected", "true")
//                            .path("/")
//                            .maxAge(Duration.ofSeconds(3)) // 특정 시간 동안만 유지
//                            .build();
//                    exchange.getResponse().addCookie(redirectCookie);
//
//                }
//                return exchange.getResponse().setComplete();
//            }
//
//        }
//
//    }
//
//    private String resolveToken(ServerHttpRequest request) {
//        // Authorization 헤더에서 JWT 토큰 추출
//        String bearerToken = request.getHeaders().getFirst(HEADER_AUTHORIZATION);
//        System.out.println("bearerToken :: " + bearerToken);
//        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    private String getTokenFromCookiesOrRequest(ServerHttpRequest request) {
//        // token이 null이면 쿠키에서 refresh token을 가져옵니다.
//        Optional<HttpCookie> refreshTokenCookie = Optional.ofNullable(request
//                .getCookies()
//                .getFirst("refreshToken")); // 쿠키 이름이 'refresh_token'이라고 가정
//
//        if (refreshTokenCookie.isPresent()) {
//            String refreshToken = refreshTokenCookie.get().getValue();
//            log.info("Refresh token from cookie :: {}", refreshToken);
//            return refreshToken;
//        }
//
//        return null;
//    }


}
