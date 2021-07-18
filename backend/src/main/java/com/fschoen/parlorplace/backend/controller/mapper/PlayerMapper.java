package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PlayerMapper {

    PlayerDTO toDTO(Player player, @Context Boolean obfuscate);

    Set<PlayerDTO> toDTO(Set<Player> players, @Context Boolean obfuscate);

    // TODO Generics?
    Map<PlayerDTO, Set<Object>> toDTO(Map<Player, Set<Object>> playerSetMap, @Context Boolean obfuscate);

}
