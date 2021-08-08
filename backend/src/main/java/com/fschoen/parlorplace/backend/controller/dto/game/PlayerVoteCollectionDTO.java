package com.fschoen.parlorplace.backend.controller.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class PlayerVoteCollectionDTO<P extends PlayerDTO<?>, T extends PlayerDTO<?>> extends VoteCollectionDTO<P> {

    @NotNull
    protected Set<T> subjects;

    @NotNull
    protected Set<T> selection;

}
