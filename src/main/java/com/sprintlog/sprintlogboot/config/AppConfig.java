package com.sprintlog.sprintlogboot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(SprintLogProperties.class)
public class AppConfig {

    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
