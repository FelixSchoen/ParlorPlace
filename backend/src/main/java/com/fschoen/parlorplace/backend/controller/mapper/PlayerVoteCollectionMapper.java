package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerVoteCollectionDTO;
import com.fschoen.parlorplace.backend.entity.PlayerVoteCollection;

public interface PlayerVoteCollectionMapper<C extends PlayerVoteCollection<?, ?>, CDTO extends PlayerVoteCollectionDTO<?, ?>, K, KDTO> extends VoteCollectionMapper<C, CDTO, K, KDTO> {
}