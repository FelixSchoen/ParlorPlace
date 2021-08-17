package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.SeerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.VillagerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.SeerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.VillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WerewolfWerewolfGameRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WerewolfGameRoleMapper extends GameRoleMapper<WerewolfGameRole, WerewolfGameRoleDTO> {

    VillagerWerewolfGameRoleDTO toDTO(VillagerWerewolfGameRole gameRole);

    WerewolfWerewolfGameRoleDTO toDTO(WerewolfWerewolfGameRole gameRole);

    SeerWerewolfGameRoleDTO toDTO(SeerWerewolfGameRole gameRole);

    default WerewolfGameRoleDTO toDTO(WerewolfGameRole gameRole) {
        switch (gameRole.getWerewolfRoleType()) {
            case VILLAGER -> {
                return toDTO((VillagerWerewolfGameRole) gameRole);
            }
            case WEREWOLF -> {
                return toDTO((WerewolfWerewolfGameRole) gameRole);
            }
            case SEER -> {
                return toDTO((SeerWerewolfGameRole) gameRole);
            }
            default -> throw new NotImplementedException("Unknown Game Role for mapping");
        }
    }

    VillagerWerewolfGameRole fromDTO(VillagerWerewolfGameRoleDTO gameRoleDTO);

    WerewolfWerewolfGameRole fromDTO(WerewolfWerewolfGameRoleDTO gameRole);

    SeerWerewolfGameRole fromDTO(SeerWerewolfGameRoleDTO gameRole);

    default WerewolfGameRole fromDTO(WerewolfGameRoleDTO gameRoleDTO) {
        switch (gameRoleDTO.getWerewolfRoleType()) {
            case VILLAGER -> {
                return fromDTO((VillagerWerewolfGameRoleDTO) gameRoleDTO);
            }
            case WEREWOLF -> {
                return fromDTO((WerewolfWerewolfGameRoleDTO) gameRoleDTO);
            }
            case SEER -> {
                return fromDTO((SeerWerewolfGameRoleDTO) gameRoleDTO);
            }
            default -> throw new NotImplementedException("Unknown Game Role for mapping");
        }
    }

}
