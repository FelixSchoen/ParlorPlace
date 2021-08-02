package com.fschoen.parlorplace.backend.integration.base;

import com.fschoen.parlorplace.backend.*;
import com.fschoen.parlorplace.backend.datagenerator.*;
import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.integration.utility.*;
import com.fschoen.parlorplace.backend.security.*;
import io.restassured.*;
import io.restassured.http.*;
import io.restassured.response.*;
import io.restassured.specification.*;
import lombok.extern.slf4j.*;
import org.assertj.core.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.web.server.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

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
