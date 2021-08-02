package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

import java.util.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, GameRoleMapper.class})
public interface PlayerMapper {

    GameRoleMapper gameRoleMapper = Mappers.getMapper(GameRoleMapper.class);

    @Mapping(target = "werewolfRoleDTO", source = "werewolfRole", qualifiedByName = "obfuscateWerewolfRole")
    WerewolfPlayerDTO toDTO(WerewolfPlayer player, @Context Boolean obfuscate);

    Set<WerewolfPlayerDTO> toDTO(Set<WerewolfPlayer> players, @Context Boolean obfuscate);

    Map<WerewolfPlayerDTO, Set<Object>> toDTO(Map<WerewolfPlayer, Set<Object>> playerSetMap, @Context Boolean obfuscate);

    WerewolfPlayer fromDTO(WerewolfPlayerDTO playerDTO);

    Set<Player> fromDTO(Set<? extends PlayerDTO> playerDTOS);

    default PlayerDTO toDTO(Player player, Boolean obfuscate) {
        if (player instanceof WerewolfPlayer) {
            return toDTO((WerewolfPlayer) player, obfuscate);
        }

        throw new MappingException(Messages.exception(MessageIdentifiers.MAPPING_TYPE));
    }

    default Player fromDTO(PlayerDTO player) {
        if (player instanceof WerewolfPlayerDTO) {
            return fromDTO((WerewolfPlayerDTO) player);
        }

        throw new MappingException(Messages.exception(MessageIdentifiers.MAPPING_TYPE));
    }

    // Named Methods

    @Named("obfuscateWerewolfRole")
    default WerewolfRoleDTO obfuscateWerewolfRole(WerewolfRole werewolfRole, @Context Boolean obfuscate) {
        if (obfuscate)
            return null;
        return gameRoleMapper.toDTO(werewolfRole);
    }

}
