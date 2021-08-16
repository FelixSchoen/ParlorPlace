package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class VoteDTO<P extends PlayerDTO<?>, T, C extends VoteCollectionDTO<P, T>, D extends Enum<D>> {

    @NotNull
    protected Long id;

    @NotNull
    protected VoteState voteState;

    @NotNull
    protected VoteType voteType;

    @NotNull
    protected D voteDescriptor;

    @NotNull
    protected Set<P> voters;

    @Valid
    @NotNull
    protected Map<Long, C> voteCollectionMap;

    @NotNull
    protected Set<T> outcome;

    @NotNull
    protected Integer outcomeAmount;

    @NotNull
    protected Instant endTime;

}
