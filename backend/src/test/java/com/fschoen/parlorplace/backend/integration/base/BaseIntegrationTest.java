package com.fschoen.parlorplace.backend.integration.base;

import com.fschoen.parlorplace.backend.ParlorPlaceApplication;
import com.fschoen.parlorplace.backend.datagenerator.DatabasePopulator;
import com.fschoen.parlorplace.backend.datagenerator.GeneratedData;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.integration.utility.TestIsolationService;
import com.fschoen.parlorplace.backend.security.JwtUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ParlorPlaceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public abstract class BaseIntegrationTest {

    protected GeneratedData generatedData;
    @Autowired
    private DatabasePopulator databasePopulator;
    @Autowired
    private TestIsolationService testIsolationService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    protected String BASE_URI = "/";
    protected String USER_BASE_URI = BASE_URI + "user/";
    protected String GAME_BASE_URI = BASE_URI + "game/";

    @LocalServerPort
    protected int port;

    @BeforeEach
    public void beforeBase() {
        testIsolationService.recreateDatabase();
        this.generatedData = databasePopulator.generate();

        RestAssured.port = port;
    }

    protected String getToken(User userInTestdata) {
        return getToken(userInTestdata.getUsername(), generatedData.getPasswordCollection().get(userInTestdata));
    }

    protected String getToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        return Strings.join("Bearer ", accessToken).with("");
    }

    protected Response post(Object o, String URI) {
        return post(o, URI, "");
    }

    protected Response get(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .get(URI)
                .then()
                .extract()
                .response();
    }

    protected Response post(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .post(URI)
                .then()
                .extract()
                .response();
    }

    protected Response put(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .put(URI)
                .then()
                .extract()
                .response();
    }

    protected Response patch(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .patch(URI)
                .then()
                .extract()
                .response();
    }

    protected Response delete(Object o, String URI, String authorization) {
        return payload(o, authorization)
                .delete(URI)
                .then()
                .extract()
                .response();
    }

    protected RequestSpecification payload(Object o, String authorization) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .body(o);
    }

}
