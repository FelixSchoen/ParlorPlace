package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.VoteCollectionMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerVoteCollectionDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerVoteCollection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WerewolfPlayerMapper.class})
public interface WerewolfPlayerVoteCollectionMapper extends VoteCollectionMapper<WerewolfPlayerVoteCollection, WerewolfPlayerVoteCollectionDTO, WerewolfPlayer, WerewolfPlayerDTO> {
}
