package com.fschoen.parlorplace.backend.controller.dto.user;

import lombok.Data;

@Data
public class UserSignupRequestDTO {

    private String username;
    private String password;
    private String email;

}
