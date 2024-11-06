package com.example.spring.edgeservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.Authentication;

@Getter
@Builder
public class AuthenticationResponseDTO {
    private Authentication authentication;
}
