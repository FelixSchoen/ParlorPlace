package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.Vote;

import java.util.List;

public interface VoteMapper<V extends Vote<?, ?, ?, ?, ?>, VDTO extends VoteDTO<?, ?, ?, ?, ?>> {

    VDTO toDTO(V vote);

    List<VDTO> toDTO(List<V> voteList);

    V fromDTO(VDTO voteDTO);

    List<V> fromDTO(List<VDTO> voteDTOList);

}
