package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.entity.persistance.GameRole;
import com.fschoen.parlorplace.backend.exception.MappingException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRole;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface GameRoleMapper {

    WerewolfRoleDTO toDTO(WerewolfRole werewolfRole);

    Set<GameRoleDTO> toDTO(Set<GameRole> gameRoles);

    default GameRoleDTO toDTO(GameRole gameRole) {
        if (gameRole instanceof WerewolfRole) {
            return toDTO((WerewolfRole) gameRole);
        }

        throw new MappingException(Messages.exception("mapping.type"));
    }

}
