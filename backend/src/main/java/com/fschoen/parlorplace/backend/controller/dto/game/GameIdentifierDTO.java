package com.fschoen.parlorplace.backend.controller.dto.game;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class GameIdentifierDTO {

    @NotNull
    @NotBlank
    @Size(min = 4)
    private String token;

}
