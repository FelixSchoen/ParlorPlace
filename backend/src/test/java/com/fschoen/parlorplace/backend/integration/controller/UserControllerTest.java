package com.fschoen.parlorplace.backend.integration.controller;

import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshResponseDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.*;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.integration.base.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest extends BaseIntegrationTest {

    @Test
    public void registerNonExistingUser_resultsInNewUserCreated() {
        UserRegisterRequestDTO userRegisterRequestDTO = UserRegisterRequestDTO.builder()
                .username("ne_user")
                .nickname("ne_user")
                .password("password")
                .email("ne_user@mail.com")
                .build();

        Response response = post(userRegisterRequestDTO, USER_BASE_URI + "register");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void registerExistingUser_resultsInDataConflictException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserRegisterRequestDTO userRegisterRequestDTO = UserRegisterRequestDTO.builder()
                .username(existingUser.getUsername())
                .nickname(existingUser.getUsername())
                .password(generatedData.getPasswordCollection().get(existingUser))
                .email(existingUser.getEmail())
                .build();

        Response response = post(userRegisterRequestDTO, USER_BASE_URI + "register");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void registerUser_withInvalidData_resultsInValidationException() {
        UserRegisterRequestDTO userRegisterRequestDTO = UserRegisterRequestDTO.builder()
                .username("ne_user")
                .password("password")
                .build();

        Response response = post(userRegisterRequestDTO, USER_BASE_URI + "register");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void loginNonExistingUser_resultsInUnauthorizedError() {
        UserLoginRequestDTO userLoginRequestDTO = UserLoginRequestDTO.builder()
                .username("ne_user")
                .password("password")
                .build();

        Response response = post(userLoginRequestDTO, USER_BASE_URI + "login");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void loginExistingUser_resultsInTokenReturned() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserLoginRequestDTO userLoginRequestDTO = UserLoginRequestDTO.builder()
                .username(existingUser.getUsername())
                .password(generatedData.getPasswordCollection().get(existingUser))
                .build();

        Response response = post(userLoginRequestDTO, USER_BASE_URI + "login");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserLoginResponseDTO userLoginResponseDTO = response.getBody().as(UserLoginResponseDTO.class);
        assertThat(userLoginResponseDTO.getAccessToken()).isNotNull();
    }

    @Test
    public void loginExistingUser_withWrongPassword_resultsInUnauthorizedError() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserLoginRequestDTO userLoginRequestDTO = UserLoginRequestDTO.builder()
                .username(existingUser.getUsername())
                .password("not" + generatedData.getPasswordCollection().get(existingUser))
                .build();

        Response response = post(userLoginRequestDTO, USER_BASE_URI + "login");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void refreshAccessToken_withValidRefreshToken_resultsInNewAccessToken() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        UserLoginRequestDTO userLoginRequestDTO = UserLoginRequestDTO.builder()
                .username(existingUser.getUsername())
                .password(generatedData.getPasswordCollection().get(existingUser))
                .build();

        Response responseSignin = post(userLoginRequestDTO, USER_BASE_URI + "login");
        UserLoginResponseDTO userLoginResponseDTO = responseSignin.getBody().as(UserLoginResponseDTO.class);

        TokenRefreshRequestDTO tokenRefreshRequestDTO = TokenRefreshRequestDTO.builder().refreshToken(userLoginResponseDTO.getRefreshToken()).build();

        Response responseRefresh = post(tokenRefreshRequestDTO, USER_BASE_URI + "refresh");
        assertThat(responseRefresh.statusCode()).isEqualTo(HttpStatus.OK.value());

        TokenRefreshResponseDTO tokenRefreshResponseDTO = responseRefresh.getBody().as(TokenRefreshResponseDTO.class);
        assertThat(tokenRefreshResponseDTO.getAccessToken()).isNotNull();
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

        Response response = payload(userUpdateRequestDTO, getToken(existingUser)).pathParam("id", otherUser.getId()).put(USER_BASE_URI + "update/{id}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
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

        Response response = payload(userUpdateRequestDTO, getToken(existingUser)).pathParam("id", existingUser.getId()).put(USER_BASE_URI + "update/{id}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserDTO returnedUser = response.getBody().as(UserDTO.class);
        assertEquals("new" + existingUser.getNickname(), returnedUser.getNickname());
        assertThat(returnedUser.getUserRoles().size()).isEqualTo(1);
    }

    @Test
    public void updateOtherUser_withEnoughAuthority_resultsInUpdatedUser() {
        User existingAdmin = this.generatedData.getUserCollection().getAdmin1();
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

        Response response = payload(userUpdateRequestDTO, getToken(existingAdmin)).pathParam("id", existingUser.getId()).put(USER_BASE_URI + "update/{id}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserDTO returnedUser = response.getBody().as(UserDTO.class);
        assertEquals("new" + existingUser.getNickname(), returnedUser.getNickname());
        assertThat(returnedUser.getUserRoles().size()).isEqualTo(1);
    }

    @Test
    public void getCurrentUser_withValidAuthorization_resultsInCurrentUser() {
        User existingUser = this.generatedData.getUserCollection().getUser1();

        Response response = get("", USER_BASE_URI + "individual", getToken(existingUser));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserDTO returnedUser = response.getBody().as(UserDTO.class);
        assertEquals(existingUser.getUsername(), returnedUser.getUsername());
    }

    @Test
    public void getUser_withValidId_resultsInFoundUser() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        User existingAdmin = this.generatedData.getUserCollection().getAdmin1();

        Response response = payload("", getToken(existingUser)).pathParam("id", existingAdmin.getId()).get(USER_BASE_URI + "individual/{id}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserDTO returnedUser = response.getBody().as(UserDTO.class);
        assertEquals(existingAdmin.getUsername(), returnedUser.getUsername());
    }

    @Test
    public void getUser_withInvalidId_resultsInDataConflictException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();

        Response response = payload("", getToken(existingUser)).pathParam("id", -1).get(USER_BASE_URI + "individual/{id}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void getUser_withValidUsername_resultsInFoundUser() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        User existingAdmin = this.generatedData.getUserCollection().getAdmin1();

        Response response = payload("", getToken(existingUser)).pathParam("username", existingAdmin.getUsername()).get(USER_BASE_URI + "individual/username/{username}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserDTO returnedUser = response.getBody().as(UserDTO.class);
        assertEquals(existingAdmin.getUsername(), returnedUser.getUsername());
    }

    @Test
    public void getUser_withInvalidUsername_resultsInDataConflictException() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        User neUser = this.generatedData.getUserCollection().getNeUser1();

        Response response = payload("", getToken(existingUser)).pathParam("username", neUser.getUsername()).get(USER_BASE_URI + "individual/username/{username}").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void getAllUsersFiltered_withValidUsernameAndNickname_returnsFoundUsers() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        User existingAdmin = this.generatedData.getUserCollection().getAdmin1();

        Response response = payload("", getToken(existingUser)).param("username", existingUser.getUsername())
                .param("nickname", existingAdmin.getNickname()).get(USER_BASE_URI + "").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Set<UserDTO> userSet = new HashSet<>(Arrays.asList(response.getBody().as(UserDTO[].class)));
        assertTrue(userSet.stream().anyMatch(x -> x.getUsername().equals(existingUser.getUsername())));
        assertTrue(userSet.stream().anyMatch(x -> x.getUsername().equals(existingAdmin.getUsername())));
    }

    @Test
    public void getAllUsersFiltered_withNonExistentUsernameAndNickname_returnsEmptySet() {
        User existingUser = this.generatedData.getUserCollection().getUser1();
        User neUser = this.generatedData.getUserCollection().getNeUser1();

        Response response = payload("", getToken(existingUser)).param("username", neUser.getUsername())
                .param("nickname", neUser.getNickname()).get(USER_BASE_URI + "").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Set<UserDTO> userSet = new HashSet<>(Arrays.asList(response.getBody().as(UserDTO[].class)));
        assertThat(userSet.size()).isEqualTo(0);
    }

    @Test
    public void getAllUsersFiltered_withQueryStringTooShort_returnsEmptySet() {
        User existingUser = this.generatedData.getUserCollection().getUser1();

        Response response = payload("", getToken(existingUser)).param("username", existingUser.getUsername().substring(0, 1)).get(USER_BASE_URI + "").then().extract().response();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Set<UserDTO> userSet = new HashSet<>(Arrays.asList(response.getBody().as(UserDTO[].class)));
        assertThat(userSet.size()).isEqualTo(0);
    }

}
