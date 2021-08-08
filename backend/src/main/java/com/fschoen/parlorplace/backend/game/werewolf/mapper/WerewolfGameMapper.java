package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameIdentifierMapper;
import com.fschoen.parlorplace.backend.controller.mapper.GameMapper;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, WerewolfPlayerMapper.class, GameIdentifierMapper.class, WerewolfLogEntryMapper.class})
public interface WerewolfGameMapper extends GameMapper<WerewolfGame, WerewolfGameDTO> {
}
