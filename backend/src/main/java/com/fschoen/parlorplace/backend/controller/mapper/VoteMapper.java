package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.Vote;
import org.mapstruct.Mapping;

import java.util.List;

public interface VoteMapper<V extends Vote<P, ?, ?>, VDTO extends VoteDTO<PDTO, ?, ?>, P extends Player<?>, PDTO extends PlayerDTO<?>> {

    @Mapping(source = "voteCollectionMap", target = "voters")
    VDTO toDTO(V vote);

    List<VDTO> toDTO(List<V> voteList);

    V fromDTO(VDTO voteDTO);

    List<V> fromDTO(List<VDTO> voteDTOList);

}
