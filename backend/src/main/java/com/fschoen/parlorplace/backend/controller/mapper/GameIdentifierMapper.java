package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameIdentifierDTO;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameIdentifierMapper {

    GameIdentifierDTO toDTO(GameIdentifier gameIdentifier);

}
