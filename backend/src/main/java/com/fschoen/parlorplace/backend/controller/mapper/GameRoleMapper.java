package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.entity.GameRole;

import java.util.List;

public interface GameRoleMapper<GR extends GameRole, GRDTO extends GameRoleDTO> {

    GRDTO toDTO(GR gameRole);

    List<GRDTO> toDTO(List<GR> gameRoles);

    GR fromDTO(GRDTO gameRoleDTO);

    List<GR> fromDTO(List<GRDTO> gameRoleDTOS);

}
