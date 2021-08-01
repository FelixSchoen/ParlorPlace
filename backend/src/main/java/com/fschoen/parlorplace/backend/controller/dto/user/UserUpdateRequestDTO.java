package com.fschoen.parlorplace.backend.controller.dto.user;

import com.fschoen.parlorplace.backend.enumeration.*;
import lombok.*;

import javax.validation.constraints.*;
import java.util.*;

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
