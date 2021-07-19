package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.exception.MappingException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRole;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class, GameRoleMapper.class})
public interface PlayerMapper {

    GameRoleMapper gameRoleMapper = Mappers.getMapper(GameRoleMapper.class);

    @Mapping(target = "werewolfRoleDTO", source = "werewolfRole", qualifiedByName = "obfuscateWerewolfRole")
    WerewolfPlayerDTO toDTO(WerewolfPlayer player, @Context Boolean obfuscate);

    Set<WerewolfPlayerDTO> toDTO(Set<WerewolfPlayer> players, @Context Boolean obfuscate);

    Map<WerewolfPlayerDTO, Set<Object>> toDTO(Map<WerewolfPlayer, Set<Object>> playerSetMap, @Context Boolean obfuscate);

    default PlayerDTO toDTO(Player player, Boolean obfuscate) {
        if (player instanceof WerewolfPlayer) {
            return toDTO((WerewolfPlayer) player, obfuscate);
        }

        throw new MappingException(Messages.exception("mapping.type"));
    }

    @Named("obfuscateWerewolfRole")
    default WerewolfRoleDTO obfuscateWerewolfRole(WerewolfRole werewolfRole, @Context Boolean obfuscate) {
        if (obfuscate)
            return null;
        return gameRoleMapper.toDTO(werewolfRole);
    }

}
