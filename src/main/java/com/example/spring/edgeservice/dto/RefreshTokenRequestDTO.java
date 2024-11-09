package com.example.spring.edgeservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenRequestDTO {
    private String refreshToken;
}
