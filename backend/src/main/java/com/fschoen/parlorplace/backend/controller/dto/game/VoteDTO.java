package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class VoteDTO<P extends PlayerDTO<?>, C extends VoteCollectionDTO<P>> {

    @NotNull
    protected Long id;

    @NotNull
    protected VoteState voteState;

    @NotNull
    protected VoteType voteType;

    @NotNull
    protected Map<P, C> voteCollectionMap;

    @NotNull
    protected LocalDateTime endTime;

}
