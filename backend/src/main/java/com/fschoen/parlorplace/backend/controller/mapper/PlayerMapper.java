package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.entity.Player;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface PlayerMapper<P extends Player<?>, PDTO extends PlayerDTO<?>> {

    @Mapping(target = "gameRoles", source = "gameRoles")
    PDTO toDTO(P player);

    Set<PDTO> toDTO(Set<P> players);

    default Set<PDTO> toDTOSet(Map<P, ?> map) {
        Set<PDTO> set = new HashSet<>();
        for (P p: map.keySet()) {
            set.add(toDTO(p));
        }
        return set;
    }

    P fromDTO(PDTO playerDTO);

    Set<P> fromDTO(Set<PDTO> playerDTOS);

}
