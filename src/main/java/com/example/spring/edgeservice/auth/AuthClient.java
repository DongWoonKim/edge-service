package com.example.spring.edgeservice.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AuthClient {

    private static final String AUTH_ROOT_API = "/auths";
    private final WebClient webClient;



}
