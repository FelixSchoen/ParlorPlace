package com.fschoen.parlorplace.backend.controller.dto.user;

import com.fschoen.parlorplace.backend.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(min = 3, max = 15)
    private String nickname;

    @NotBlank
    @Size(min = 3, max = 255)
    @Email
    private String email;

    @NotNull
    private Set<UserRole> roles;

}
