package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class, GameIdentifierMapper.class})
public interface GameMapper {

    WerewolfGameDTO toDTO(WerewolfGame game, @Context Boolean obfuscate);

    default GameDTO toDTO(Game game, @Context Boolean obfuscate) {
        if (game instanceof WerewolfGame) {
            return toDTO((WerewolfGame) game, obfuscate);
        }

        throw new MappingException(Messages.exception("mapping.type"));
    }

}
