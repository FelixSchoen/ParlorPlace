package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.entity.VoteCollection;

import java.util.List;
import java.util.Map;

public interface VoteCollectionMapper<C extends VoteCollection<?>, CDTO extends VoteCollectionDTO<?, ?>, K, KDTO> {

    CDTO toDTO(C collection);

    List<CDTO> toDTO(List<C> collectionList);

    Map<KDTO, CDTO> toDTO(Map<K, C> collectionMap);

    C fromDTO(CDTO collectionDTO);

    List<C> fromDTO(List<CDTO> collectionDTOList);

    Map<K, C> fromDTO(Map<KDTO, CDTO> collectionDTOMap);

}
