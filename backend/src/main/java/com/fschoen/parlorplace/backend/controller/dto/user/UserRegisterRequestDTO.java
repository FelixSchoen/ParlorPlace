package com.fschoen.parlorplace.backend.controller.dto.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public class UserRegisterRequestDTO extends BaseUserInformationDTO {

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

}
