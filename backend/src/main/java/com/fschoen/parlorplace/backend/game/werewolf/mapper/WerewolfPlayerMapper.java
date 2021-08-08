package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapper;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WerewolfGameMapper.class, WerewolfGameRoleMapper.class, UserMapper.class,})
public interface WerewolfPlayerMapper extends PlayerMapper<WerewolfPlayer, WerewolfPlayerDTO> {

}
