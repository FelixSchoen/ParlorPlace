package com.fschoen.parlorplace.backend.game.werewolf.dto.role;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "werewolfRoleType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VillagerWerewolfGameRoleDTO.class, name = "VILLAGER"),
        @JsonSubTypes.Type(value = WerewolfWerewolfGameRoleDTO.class, name = "WEREWOLF"),
        @JsonSubTypes.Type(value = SeerWerewolfGameRoleDTO.class, name = "SEER"),
        @JsonSubTypes.Type(value = WitchWerewolfGameRoleDTO.class, name = "WITCH"),
        @JsonSubTypes.Type(value = CupidWerewolfGameRoleDTO.class, name = "CUPID")}
)
@Data
public abstract class WerewolfGameRoleDTO extends GameRoleDTO {

    public abstract WerewolfRoleType getWerewolfRoleType();

    public abstract WerewolfFaction getWerewolfFaction();

    public abstract void setWerewolfRoleType(WerewolfRoleType werewolfRoleType);

    public abstract void setWerewolfFaction(WerewolfFaction werewolfFaction);

}
