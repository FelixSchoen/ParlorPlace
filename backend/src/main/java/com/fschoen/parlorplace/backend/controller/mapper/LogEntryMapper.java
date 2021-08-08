package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.LogEntryDTO;
import com.fschoen.parlorplace.backend.entity.LogEntry;

import java.util.List;

public interface LogEntryMapper<L extends LogEntry<?>, LDTO extends LogEntryDTO<?>> {

    LDTO toDTO(L logEntry);

    List<LDTO> toDTO(List<L> logEntries);

    L fromDTO(LDTO logEntryDTO);

    List<L> fromDTO(List<LDTO> logEntryDTOS);

}
