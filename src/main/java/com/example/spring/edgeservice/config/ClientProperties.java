package com.example.spring.edgeservice.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@Getter
@Setter
@ConfigurationProperties(prefix = "polar")
public class ClientProperties {
    @NotNull
    URI authServiceUri;
}
