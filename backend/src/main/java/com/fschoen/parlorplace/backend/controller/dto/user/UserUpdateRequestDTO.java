package com.fschoen.parlorplace.backend.controller.dto.user;

import com.fschoen.parlorplace.backend.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserUpdateRequestDTO {

    @NotNull
    private Long id;

    @Size(min = 3, max = 15)
    private String username;

    @Size(min = 8, max = 255)
    private String password;

    @Size(min = 3, max = 15)
    private String nickname;

    @Size(min = 3, max = 255)
    @Email
    private String email;

    private Set<UserRole> roles;

}
