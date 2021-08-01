package com.fschoen.parlorplace.backend;

import com.fschoen.parlorplace.backend.datagenerator.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;

@SpringBootApplication
@Profile("setup")
public class SetupApplication implements CommandLineRunner {

    private final DatabasePopulator databasePopulator;

    @Autowired
    public SetupApplication(DatabasePopulator databasePopulator) {
        this.databasePopulator = databasePopulator;
    }

    public static void main(String[] args) {
        SpringApplication.run(SetupApplication.class, args);
    }

    @Override
    public void run(String... args) {
        databasePopulator.generate();
    }
}
