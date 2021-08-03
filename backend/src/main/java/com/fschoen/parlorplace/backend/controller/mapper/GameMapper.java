package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.entity.Game;

public interface GameMapper<G extends Game<?, ?>, GDTO extends GameDTO<?, ?>> {

    GDTO toDTO(G game);

    G fromDTO(GDTO gameDTO);

}
