package com.fschoen.parlorplace.backend;

import com.fschoen.parlorplace.backend.datagenerator.DatabasePopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("setup")
public class SetupApplication implements CommandLineRunner {

    private final DatabasePopulator databasePopulator;

    @Autowired
    public SetupApplication(DatabasePopulator databasePopulator) {
        this.databasePopulator = databasePopulator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParlorPlaceApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        databasePopulator.generate();
    }
}
