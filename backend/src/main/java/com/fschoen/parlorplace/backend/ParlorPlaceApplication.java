package com.fschoen.parlorplace.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("!setup")
public class ParlorPlaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParlorPlaceApplication.class, args);
    }

}
