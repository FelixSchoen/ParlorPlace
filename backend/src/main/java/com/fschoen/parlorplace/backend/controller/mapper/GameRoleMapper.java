package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.mapstruct.*;

import java.util.*;

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
