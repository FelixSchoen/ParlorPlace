package com.fschoen.parlorplace.backend.game.werewolf.dto.role;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "werewolfRoleType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VillagerWerewolfGameRoleDTO.class, name = "VILLAGER"),
        @JsonSubTypes.Type(value = WerewolfWerewolfGameRoleDTO.class, name = "WEREWOLF"),
        @JsonSubTypes.Type(value = SeerWerewolfGameRoleDTO.class, name = "SEER")}
)
@Data
public abstract class WerewolfGameRoleDTO extends GameRoleDTO {

    @NotNull
    protected WerewolfRoleType werewolfRoleType;

    @NotNull
    protected WerewolfFaction werewolfFaction;

}
