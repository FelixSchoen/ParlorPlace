package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.Vote;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfVote extends Vote<WerewolfPlayer, WerewolfPlayerVoteCollection> {
}
