package com.fschoen.parlorplace.backend.controller.dto.user;

import com.fschoen.parlorplace.backend.enumeration.*;
import lombok.*;

import javax.validation.constraints.*;
import java.util.*;

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
