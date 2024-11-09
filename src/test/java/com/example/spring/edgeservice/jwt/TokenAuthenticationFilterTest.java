package com.example.spring.edgeservice.jwt;

import com.example.spring.edgeservice.auth.AuthClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {

    @Mock
    private AuthClient authClient;

//    @Test
    public void testValidToken_Success() {
//        // given
//        String refreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJnYXVyaTc4OTFAZ21haWwuY29tIiwiaWF0IjoxNzMwODY0MTk3LCJleHAiOjE3MzEwMzY5OTcsInN1YiI6InRlc3QiLCJpZCI6MSwicm9sZSI6IlJPTEVfVVNFUiIsInVzZXJOYW1lIjoidGVzdCJ9.TDcCXG7br5bAbwS2WnM0sO00d4Yt2iio6DFIq2rx0nxp3YVW-Qbgv5mP98OpnIootdxkaIcb0sAFlhFVMtzQ4Q";
//        ValidTokenRequestDTO requestDTO = ValidTokenRequestDTO.builder()
//                .refreshToken(refreshToken)
//                .build();
//
//        // Mocking: authClient의 validToken 메서드가 예상 응답을 반환하도록 설정
//        ValidTokenResponseDTO expectedResponse = ValidTokenResponseDTO.builder()
//                .statusNum(1)
//                .build();
//        given(authClient.validToken(requestDTO)).willReturn(expectedResponse);
//
//        // When
//        ValidTokenResponseDTO validTokenResponseDTO = authClient.validToken(requestDTO);
//
//        // Then
//        assertEquals(1, validTokenResponseDTO.getStatusNum());
//        System.out.println(validTokenResponseDTO.getStatusNum());
    }

}