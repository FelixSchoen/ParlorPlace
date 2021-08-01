package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.game.management.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GameIdentifierMapper {

    GameIdentifierDTO toDTO(GameIdentifier gameIdentifier);

}
