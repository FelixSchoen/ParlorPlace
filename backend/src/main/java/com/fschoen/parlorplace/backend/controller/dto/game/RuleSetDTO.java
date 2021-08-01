package com.fschoen.parlorplace.backend.controller.dto.game;

import lombok.*;
import lombok.experimental.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class RuleSetDTO {

    @NotNull
    protected Long id;

}
