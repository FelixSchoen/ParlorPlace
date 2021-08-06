package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.LogEntryMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfLogEntryDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfLogEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WerewolfPlayerMapper.class})
public interface WerewolfLogEntryMapper extends LogEntryMapper<WerewolfLogEntry, WerewolfLogEntryDTO> {
}
