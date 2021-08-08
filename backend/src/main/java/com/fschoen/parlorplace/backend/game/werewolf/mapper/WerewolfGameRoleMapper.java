package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.SeerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.VillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WerewolfWerewolfGameRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WerewolfGameRoleMapper extends GameRoleMapper<WerewolfGameRole, WerewolfGameRoleDTO> {

    WerewolfGameRoleDTO toDTO(VillagerWerewolfGameRole gameRole);

    WerewolfGameRoleDTO toDTO(WerewolfWerewolfGameRole gameRole);

    WerewolfGameRoleDTO toDTO(SeerWerewolfGameRole gameRole);

    default WerewolfGameRole fromDTO(WerewolfGameRoleDTO gameRoleDTO) {
        switch (gameRoleDTO.getWerewolfRoleType()) {
            case VILLAGER -> {
                return VillagerWerewolfGameRole.builder().build();
            }
            case WEREWOLF -> {
                return WerewolfWerewolfGameRole.builder().build();
            }
            case SEER -> {
                return SeerWerewolfGameRole.builder().build();
            }
            default -> throw new NotImplementedException("Unknown Game Role for mapping");
        }
    }

}
