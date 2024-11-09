package com.example.spring.edgeservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RefreshTokenResponseDTO {
    private int status;
    private String accessToken;
    private String refreshToken;
}
