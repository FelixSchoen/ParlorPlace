package com.fschoen.parlorplace.backend.controller.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserSignupRequestDTO {

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

    @NotBlank
    @Size(min = 3, max = 15)
    private String nickname;

    @NotBlank
    @Size(min = 3, max = 255)
    @Email
    private String email;

}
