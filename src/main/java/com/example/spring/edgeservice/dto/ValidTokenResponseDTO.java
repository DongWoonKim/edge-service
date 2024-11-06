package com.example.spring.edgeservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidTokenResponseDTO {
    private int statusNum;  // 1: 유효, 2: 만료, 3: 유효하지 않음
    private String message;
}
