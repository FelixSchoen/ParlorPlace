package com.fschoen.parlorplace.backend.controller.dto.user;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserLoginRequestDTO {

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

}
