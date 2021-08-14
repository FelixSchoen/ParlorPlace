package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fschoen.parlorplace.backend.configuration.PlayerDTOSerialization;
import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    @JsonSerialize(keyUsing = PlayerDTOSerialization.PlayerDTOKeySerializer.class)
    @JsonDeserialize(keyUsing = PlayerDTOSerialization.PlayerDTOKeyDeserializer.class)
    @Valid
    @NotNull
    protected Map<P, C> voteCollectionMap;

    @NotNull
    protected Set<T> outcome;

    @NotNull
    protected LocalDateTime endTime;

}
