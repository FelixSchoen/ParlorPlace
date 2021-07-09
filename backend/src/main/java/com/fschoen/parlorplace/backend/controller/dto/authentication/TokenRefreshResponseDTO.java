package com.fschoen.parlorplace.backend.controller.dto.authentication;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Data
public class TokenRefreshResponseDTO {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

}
