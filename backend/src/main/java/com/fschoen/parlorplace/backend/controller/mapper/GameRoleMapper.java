package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.entity.GameRole;

public interface GameRoleMapper<GR extends GameRole, GRDTO extends GameRoleDTO> {

    GRDTO toDTO(GR gameRole);

    GR fromDTO(GRDTO gameRoleDTO);

}
