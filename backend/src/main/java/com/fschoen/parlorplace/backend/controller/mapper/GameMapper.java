package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.entity.Game;

import java.util.List;

public interface GameMapper<G extends Game<?, ?, ?>, GDTO extends GameDTO<?, ?, ?>> {

    GDTO toDTO(G game);

    List<GDTO> toDTO(List<G> games);

    G fromDTO(GDTO gameDTO);

}
