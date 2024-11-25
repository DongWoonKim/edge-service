package com.example.spring.edgeservice.config;

import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.Redisson;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.username:user}")
    private String redisUsername;

    @Value("${spring.data.redis.password:1234}")
    private String redisPassword;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        logger.info("Configuring Redisson Client...");
        logger.info("Redis Host (raw): {}", redisHost);
        logger.info("Redis Port: {}", redisPort);

        // Remove `redis://` if already included in redisHost
        String cleanedHost = redisHost.trim();
        if (cleanedHost.startsWith("redis://")) {
            cleanedHost = cleanedHost.substring("redis://".length());
        }

        // Generate Redis URL
        String redisUrl = String.format("redis://%s:%d", cleanedHost, redisPort);
        logger.info("Redis URL: {}", redisUrl);

        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisUrl)
                .setUsername(redisUsername.isEmpty() ? null : redisUsername)
                .setPassword(redisPassword.isEmpty() ? null : redisPassword)
                .setDnsMonitoringInterval(-1);

        logger.info("RedissonClient created successfully.");
        return Redisson.create(config);
    }

}
