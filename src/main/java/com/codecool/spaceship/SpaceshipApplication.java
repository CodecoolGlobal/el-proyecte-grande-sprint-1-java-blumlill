package com.codecool.spaceship;

import com.codecool.spaceship.model.base.Base;
import com.codecool.spaceship.model.initializer.Initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpaceshipApplication implements CommandLineRunner {

    private Initializer initializer;

    @Autowired
    public SpaceshipApplication(Initializer initializer) {
        this.initializer = initializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpaceshipApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initializer.initData();
    }

    @Bean
    public Base base() {
        return new Base("Base One");
    }
}
