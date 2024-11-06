package com.example.spring.edgeservice.auth;

import com.example.spring.edgeservice.dto.AuthenticationRequestDTO;
import com.example.spring.edgeservice.dto.AuthenticationResponseDTO;
import com.example.spring.edgeservice.dto.ValidTokenRequestDTO;
import com.example.spring.edgeservice.dto.ValidTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authClient", url = "${polar.auth-service-uri}")
public interface AuthClient {
    @PostMapping("/auths/validToken")
    ValidTokenResponseDTO validToken(@RequestBody ValidTokenRequestDTO requestDTO);

    @PostMapping("/auths/authentication")
    AuthenticationResponseDTO getAuthentication(@RequestBody AuthenticationRequestDTO requestDTO);
}
