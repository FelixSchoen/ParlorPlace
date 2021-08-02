package com.fschoen.parlorplace.backend.controller.dto.user;

import com.fschoen.parlorplace.backend.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserLoginResponseDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotNull
    private Set<UserRole> roles;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

}
