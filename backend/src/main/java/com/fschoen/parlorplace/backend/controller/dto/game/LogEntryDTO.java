package com.fschoen.parlorplace.backend.controller.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public class LogEntryDTO<P extends PlayerDTO<?>> {

    @NotNull
    protected UUID identifier;

    @NotNull
    protected Set<P> recipients;

    @NotNull
    protected Set<P> sources;

    @NotNull
    protected Set<P> targets;

}
