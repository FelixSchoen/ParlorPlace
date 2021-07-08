package com.fschoen.parlorplace.backend.integration.controller;

import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends BaseIntegrationTest {

    @Test
    public void signupNonExistingUser_resultsInNewUserCreated() {
        UserSignupRequestDTO userSignupRequestDTO = UserSignupRequestDTO.builder()
                .username("ne_user")
                .nickname("ne_user")
                .password("password")
                .email("ne_user@mail.com")
                .build();

        Response response = post(userSignupRequestDTO, USER_BASE_URI + "/signup");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void signupExistingUser_resultsInDataConflictException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserSignupRequestDTO userSignupRequestDTO = UserSignupRequestDTO.builder()
                .username(existingUser.getUsername())
                .nickname(existingUser.getUsername())
                .password(generatedData.getPasswordCollection().get(existingUser))
                .email(existingUser.getEmail())
                .build();

        Response response = post(userSignupRequestDTO, USER_BASE_URI + "/signup");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void signupUser_withInvalidData_resultsInValidationException() {
        UserSignupRequestDTO userSignupRequestDTO = UserSignupRequestDTO.builder()
                .username("ne_user")
                .password("password")
                .build();

        Response response = post(userSignupRequestDTO, USER_BASE_URI + "/signup");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
