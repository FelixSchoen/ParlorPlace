package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.VoteCollectionMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteCollectionDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVoteCollection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WerewolfPlayerMapper.class})
public interface WerewolfVoteCollectionMapper extends VoteCollectionMapper<WerewolfVoteCollection, WerewolfVoteCollectionDTO, WerewolfPlayer, WerewolfPlayerDTO> {
}
