package com.fschoen.parlorplace.backend.controller.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class TokenRefreshResponseDTO {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

}
