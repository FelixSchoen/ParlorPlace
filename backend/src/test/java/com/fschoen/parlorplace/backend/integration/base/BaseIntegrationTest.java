package com.fschoen.parlorplace.backend.integration.base;

import com.fschoen.parlorplace.backend.ParlorPlaceApplication;
import com.fschoen.parlorplace.backend.datagenerator.DatabasePopulator;
import com.fschoen.parlorplace.backend.datagenerator.GeneratedData;
import com.fschoen.parlorplace.backend.integration.utility.TestIsolationService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.Before;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ParlorPlaceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    private DatabasePopulator databasePopulator;

    protected GeneratedData generatedData;

    @Autowired
    private TestIsolationService testIsolationService;

    protected String BASE_URI = "";
    protected String USER_BASE_URI = "/user";

    @LocalServerPort
    protected int port;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @BeforeEach
    public void beforeBase() {
        testIsolationService.recreateDatabase();
        this.generatedData = databasePopulator.generate();

        RestAssured.port = port;
    }

    protected Response post(Object o, String URI) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(o)
                .post(URI)
                .then()
                .extract()
                .response();
    }

}
