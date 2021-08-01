package com.fschoen.parlorplace.backend.controller.dto.user;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserRegisterRequestDTO {

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
