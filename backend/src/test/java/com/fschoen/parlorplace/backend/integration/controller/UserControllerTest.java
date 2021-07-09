package com.fschoen.parlorplace.backend.integration.controller;

import com.fschoen.parlorplace.backend.controller.dto.user.*;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enums.UserRole;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void signinNonExistingUser_resultsInUnauthorizedError() {
        UserSigninRequestDTO userSigninRequestDTO = UserSigninRequestDTO.builder()
                .username("ne_user")
                .password("password")
                .build();

        Response response = post(userSigninRequestDTO, USER_BASE_URI + "/signin");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void signinExistingUser_resultsInTokenReturned() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserSigninRequestDTO userSigninRequestDTO = UserSigninRequestDTO.builder()
                .username(existingUser.getUsername())
                .password(generatedData.getPasswordCollection().get(existingUser))
                .build();

        Response response = post(userSigninRequestDTO, USER_BASE_URI + "/signin");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserSigninResponseDTO userSigninResponseDTO = response.getBody().as(UserSigninResponseDTO.class);
        assertThat(userSigninResponseDTO.getAccessToken()).isNotNull();
    }

    @Test
    public void signinExistingUser_withWrongPassword_resultsInUnauthorizedError() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserSigninRequestDTO userSigninRequestDTO = UserSigninRequestDTO.builder()
                .username(existingUser.getUsername())
                .password("not" + generatedData.getPasswordCollection().get(existingUser))
                .build();

        Response response = post(userSigninRequestDTO, USER_BASE_URI + "/signin");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void updateExistingUser_withNotEnoughAuthority_resultsInAuthorizationException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        User otherUser = this.generatedData.getUserCollection().getAdmin1();
        UserUpdateRequestDTO userUpdateRequestDTO = UserUpdateRequestDTO.builder()
                .id(otherUser.getId())
                .username(otherUser.getUsername())
                .nickname("new" + otherUser.getNickname())
                .password("new" + generatedData.getPasswordCollection().get(otherUser))
                .email("new" + otherUser.getEmail())
                .build();

        Response response = put(userUpdateRequestDTO, USER_BASE_URI + "/update", getToken(existingUser));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void updateOwnUser_resultsInUpdatedUser() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserUpdateRequestDTO userUpdateRequestDTO = UserUpdateRequestDTO.builder()
                .id(existingUser.getId())
                .username(existingUser.getUsername())
                .nickname("new" + existingUser.getNickname())
                .password("new" + generatedData.getPasswordCollection().get(existingUser))
                .email("new" + existingUser.getEmail())
                .roles(new HashSet<>() {{
                    add(UserRole.ROLE_USER);
                }})
                .build();

        Response response = put(userUpdateRequestDTO, USER_BASE_URI + "/update", getToken(existingUser));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserDTO returnedUser = response.getBody().as(UserDTO.class);
        assertEquals("new" + existingUser.getNickname(), returnedUser.getNickname());
        assertThat(returnedUser.getRoles().size()).isEqualTo(1);
    }

}
