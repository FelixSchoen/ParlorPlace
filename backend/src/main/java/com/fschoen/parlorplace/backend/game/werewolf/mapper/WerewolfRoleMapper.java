package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {GameRoleMapper.class})
public interface WerewolfRoleMapper {

    WerewolfRoleDTO toDTO(WerewolfRole werewolfRole);

}
