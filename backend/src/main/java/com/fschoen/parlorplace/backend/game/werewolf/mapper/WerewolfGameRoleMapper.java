package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.BearTamerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.BodyguardWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.CupidWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.LycanthropeWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.SeerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.VillagerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WitchWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.BearTamerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.BodyguardWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.CupidWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.LycanthropeWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.SeerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.VillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WerewolfWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WitchWerewolfGameRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WerewolfPlayerMapper.class})
public interface WerewolfGameRoleMapper extends GameRoleMapper<WerewolfGameRole, WerewolfGameRoleDTO> {

    VillagerWerewolfGameRoleDTO toDTO(VillagerWerewolfGameRole gameRole);

    WerewolfWerewolfGameRoleDTO toDTO(WerewolfWerewolfGameRole gameRole);

    SeerWerewolfGameRoleDTO toDTO(SeerWerewolfGameRole gameRole);

    WitchWerewolfGameRoleDTO toDTO(WitchWerewolfGameRole gameRole);

    CupidWerewolfGameRoleDTO toDTO(CupidWerewolfGameRole gameRole);

    BodyguardWerewolfGameRoleDTO toDTO(BodyguardWerewolfGameRole gameRole);

    LycanthropeWerewolfGameRoleDTO toDTO(LycanthropeWerewolfGameRole gameRole);

    BearTamerWerewolfGameRoleDTO toDTO(BearTamerWerewolfGameRole gameRole);

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
            case WITCH -> {
                return toDTO((WitchWerewolfGameRole) gameRole);
            }
            case CUPID -> {
                return toDTO((CupidWerewolfGameRole) gameRole);
            }
            case BODYGUARD -> {
                return toDTO((BodyguardWerewolfGameRole) gameRole);
            }
            case LYCANTHROPE -> {
                return toDTO((LycanthropeWerewolfGameRole) gameRole);
            }
            case BEAR_TAMER -> {
                return toDTO((BearTamerWerewolfGameRole) gameRole);
            }
            default -> throw new NotImplementedException("Unknown Game Role for mapping");
        }
    }

    VillagerWerewolfGameRole fromDTO(VillagerWerewolfGameRoleDTO gameRoleDTO);

    WerewolfWerewolfGameRole fromDTO(WerewolfWerewolfGameRoleDTO gameRole);

    SeerWerewolfGameRole fromDTO(SeerWerewolfGameRoleDTO gameRole);

    WitchWerewolfGameRole fromDTO(WitchWerewolfGameRoleDTO gameRole);

    CupidWerewolfGameRole fromDTO(CupidWerewolfGameRoleDTO gameRole);

    BodyguardWerewolfGameRole fromDTO(BodyguardWerewolfGameRoleDTO gameRole);

    LycanthropeWerewolfGameRole fromDTO(LycanthropeWerewolfGameRoleDTO gameRole);

    BearTamerWerewolfGameRole fromDTO(BearTamerWerewolfGameRoleDTO gameRole);

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
            case WITCH -> {
                return fromDTO((WitchWerewolfGameRoleDTO) gameRoleDTO);
            }
            case CUPID -> {
                return fromDTO((CupidWerewolfGameRoleDTO) gameRoleDTO);
            }
            case BODYGUARD -> {
                return fromDTO((BodyguardWerewolfGameRoleDTO) gameRoleDTO);
            }
            case LYCANTHROPE -> {
                return fromDTO((LycanthropeWerewolfGameRoleDTO) gameRoleDTO);
            }
            case BEAR_TAMER -> {
                return fromDTO((BearTamerWerewolfGameRoleDTO) gameRoleDTO);
            }
            default -> throw new NotImplementedException("Unknown Game Role for mapping");
        }
    }

}
