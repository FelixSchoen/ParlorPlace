package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.entity.persistance.GameRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameRoleMapper {

    GameRoleDTO toDTO(GameRole gameRole);

}
