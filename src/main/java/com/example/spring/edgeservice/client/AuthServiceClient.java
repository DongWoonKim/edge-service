package com.example.spring.edgeservice.client;

import com.example.spring.edgeservice.dto.ValidTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AuthServiceClient {

    private final WebClient authClient;

    /**
     * 토큰 검증 후 상태 코드를 반환 (1: 유효, 2: 무효, -1: 오류)
     */
    public int validToken(String token) {
        try {
            ValidTokenResponseDTO response = authClient.post()
                    .uri("/auths/validToken")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(ValidTokenResponseDTO.class)
                    .block(); // 동기식 호출

            return response != null ? response.getStatusNum() : -1; // 응답이 없을 경우 -1 반환
        } catch (Exception e) {
            // 로그와 예외 처리
            e.printStackTrace();
            return -1; // 오류 발생 시 -1 반환
        }
    }

}
