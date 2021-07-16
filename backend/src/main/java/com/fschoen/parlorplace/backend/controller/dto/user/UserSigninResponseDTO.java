package com.fschoen.parlorplace.backend.controller.dto.user;

import com.fschoen.parlorplace.backend.enumeration.UserRole;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@Data
public class UserSigninResponseDTO {

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
