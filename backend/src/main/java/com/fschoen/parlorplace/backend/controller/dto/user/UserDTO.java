package com.fschoen.parlorplace.backend.controller.dto.user;

import com.fschoen.parlorplace.backend.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public class UserDTO extends BaseUserInformationDTO {

    @NotNull
    private Long id;

    @NotNull
    private Set<UserRole> userRoles;

}
