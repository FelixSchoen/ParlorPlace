package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.BearTamerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.BodyguardWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.CupidWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.HunterWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.LycanthropeWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.PureVillagerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.SeerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.VillagerWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.WerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.WerewolfWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.WitchWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.BearTamerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.BodyguardWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.CupidWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.HunterWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.LycanthropeWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.PureVillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.SeerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.VillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WerewolfWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WitchWerewolfGameRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WerewolfGameRoleMapper extends GameRoleMapper<WerewolfGameRole, WerewolfGameRoleDTO> {

    VillagerWerewolfGameRoleDTO toDTO(VillagerWerewolfGameRole gameRole);

    PureVillagerWerewolfGameRoleDTO toDTO(PureVillagerWerewolfGameRole gameRole);

    WerewolfWerewolfGameRoleDTO toDTO(WerewolfWerewolfGameRole gameRole);

    SeerWerewolfGameRoleDTO toDTO(SeerWerewolfGameRole gameRole);

    WitchWerewolfGameRoleDTO toDTO(WitchWerewolfGameRole gameRole);

    HunterWerewolfGameRoleDTO toDTO(HunterWerewolfGameRole gameRole);

    CupidWerewolfGameRoleDTO toDTO(CupidWerewolfGameRole gameRole);

    BodyguardWerewolfGameRoleDTO toDTO(BodyguardWerewolfGameRole gameRole);

    LycanthropeWerewolfGameRoleDTO toDTO(LycanthropeWerewolfGameRole gameRole);

    BearTamerWerewolfGameRoleDTO toDTO(BearTamerWerewolfGameRole gameRole);

    default WerewolfGameRoleDTO toDTO(WerewolfGameRole gameRole) {
        switch (gameRole.getWerewolfRoleType()) {
            case VILLAGER -> {
                return toDTO((VillagerWerewolfGameRole) gameRole);
            }
            case PURE_VILLAGER -> {
                return toDTO((PureVillagerWerewolfGameRole) gameRole);
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
            case HUNTER -> {
                return toDTO((HunterWerewolfGameRole) gameRole);
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

    PureVillagerWerewolfGameRole fromDTO(PureVillagerWerewolfGameRoleDTO gameRoleDTO);

    WerewolfWerewolfGameRole fromDTO(WerewolfWerewolfGameRoleDTO gameRole);

    SeerWerewolfGameRole fromDTO(SeerWerewolfGameRoleDTO gameRole);

    WitchWerewolfGameRole fromDTO(WitchWerewolfGameRoleDTO gameRole);

    HunterWerewolfGameRole fromDTO(HunterWerewolfGameRoleDTO gameRole);

    CupidWerewolfGameRole fromDTO(CupidWerewolfGameRoleDTO gameRole);

    BodyguardWerewolfGameRole fromDTO(BodyguardWerewolfGameRoleDTO gameRole);

    LycanthropeWerewolfGameRole fromDTO(LycanthropeWerewolfGameRoleDTO gameRole);

    BearTamerWerewolfGameRole fromDTO(BearTamerWerewolfGameRoleDTO gameRole);

    default WerewolfGameRole fromDTO(WerewolfGameRoleDTO gameRoleDTO) {
        switch (gameRoleDTO.getWerewolfRoleType()) {
            case VILLAGER -> {
                return fromDTO((VillagerWerewolfGameRoleDTO) gameRoleDTO);
            }
            case PURE_VILLAGER -> {
                return fromDTO((PureVillagerWerewolfGameRoleDTO) gameRoleDTO);
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
            case HUNTER -> {
                return fromDTO((HunterWerewolfGameRoleDTO) gameRoleDTO);
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
