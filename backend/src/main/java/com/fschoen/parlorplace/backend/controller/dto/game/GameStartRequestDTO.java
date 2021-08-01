package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.enumeration.*;
import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class GameStartRequestDTO {

    @NotNull
    GameType gameType;

}
