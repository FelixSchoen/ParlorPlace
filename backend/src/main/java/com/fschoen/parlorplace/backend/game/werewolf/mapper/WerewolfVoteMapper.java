package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.VoteMapper;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerWerewolfVoteDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerWerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfVote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WerewolfPlayerMapper.class, WerewolfVoteCollectionMapper.class})
public interface WerewolfVoteMapper extends VoteMapper<WerewolfVote<?, ?>, WerewolfVoteDTO<?, ?>> {

    WerewolfPlayerWerewolfVoteDTO toDTO(WerewolfPlayerWerewolfVote vote);

    default WerewolfVoteDTO<?,?> toDTO(WerewolfVote<?,?> vote) {
        switch(vote.getVoteIdentifier()) {
            case PLAYER_VOTE -> {
                return toDTO((WerewolfPlayerWerewolfVote) vote);
            }
            default -> throw new NotImplementedException("Unknown Vote for mapping");
        }
    }

    WerewolfPlayerWerewolfVote fromDTO(WerewolfPlayerWerewolfVoteDTO vote);

    default WerewolfVote<?,?> fromDTO(WerewolfVoteDTO<?,?> voteDTO) {
        switch(voteDTO.getVoteIdentifier()) {
            case PLAYER_VOTE -> {
                return fromDTO((WerewolfPlayerWerewolfVoteDTO) voteDTO);
            }
            default -> throw new NotImplementedException("Unknown Vote for mapping");
        }
    }

}
