package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.VoteMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WerewolfPlayerVoteCollectionMapper.class})
public interface WerewolfVoteMapper extends VoteMapper<WerewolfVote, WerewolfVoteDTO> {
}
