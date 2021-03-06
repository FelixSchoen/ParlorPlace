package com.fschoen.parlorplace.backend.controller.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class LogEntryDTO<P extends PlayerDTO<?>> {

    @NotNull
    protected UUID identifier;

    @NotNull
    protected Set<P> recipients;

    @Builder.Default
    @Valid
    @NotNull
    protected Set<P> sources = new HashSet<>();

    @Builder.Default
    @Valid
    @NotNull
    protected Set<P> targets = new HashSet<>();

}
