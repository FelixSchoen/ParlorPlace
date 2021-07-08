package com.fschoen.parlorplace.backend.integration.base;

import com.fschoen.parlorplace.backend.ParlorPlaceApplication;
import com.fschoen.parlorplace.backend.datagenerator.DatabasePopulator;
import com.fschoen.parlorplace.backend.datagenerator.GeneratedData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.Before;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ParlorPlaceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    private DatabasePopulator databasePopulator;

    protected GeneratedData generatedData;

    protected String BASE_URI = "";
    protected String USER_BASE_URI = "/user";

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Before
    public void beforeBase() {
        this.generatedData = databasePopulator.generate();
    }

}
