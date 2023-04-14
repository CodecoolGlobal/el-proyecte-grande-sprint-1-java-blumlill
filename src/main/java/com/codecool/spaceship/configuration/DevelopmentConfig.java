package com.codecool.spaceship.configuration;

import com.codecool.spaceship.model.initializer.Initializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DevelopmentConfig {
    @Bean
    public Initializer getInitializer() {
        return () -> {
            // create bases / hangars / ships
        };
    }
}
