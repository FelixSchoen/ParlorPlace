package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.entity.Player;
import org.mapstruct.Mapping;

import java.util.Set;

public interface PlayerMapper<P extends Player<?>, PDTO extends PlayerDTO> {

    @Mapping(target = "gameRoleDTO", source = "gameRole")
    PDTO toDTO(P player);

    Set<PDTO> toDTO(Set<P> players);

    P fromDTO(PDTO playerDTO);

    Set<P> fromDTO(Set<PDTO> playerDTOS);

}
