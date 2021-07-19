package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapper;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class, UserMapper.class})
public interface WerewolfPlayerMapper {

    WerewolfPlayerDTO toDTO(WerewolfPlayer werewolfPlayer, @Context Boolean obfuscate);

}
