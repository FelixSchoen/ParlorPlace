package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WerewolfGameRoleMapper extends GameRoleMapper<WerewolfGameRole, WerewolfGameRoleDTO> {
}
