package com.fschoen.parlorplace.backend.controller.dto.authentication;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class TokenRefreshRequestDTO {

    @NotBlank
    private String refreshToken;

}
