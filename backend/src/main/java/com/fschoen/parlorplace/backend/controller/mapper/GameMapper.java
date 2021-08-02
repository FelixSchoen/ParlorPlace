package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.exception.MappingException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifiers;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class, GameIdentifierMapper.class})
public interface GameMapper {

    WerewolfGameDTO toDTO(WerewolfGame game, @Context Boolean obfuscate);

    default GameDTO toDTO(Game game, @Context Boolean obfuscate) {
        if (game instanceof WerewolfGame) {
            return toDTO((WerewolfGame) game, obfuscate);
        }

        throw new MappingException(Messages.exception(MessageIdentifiers.MAPPING_TYPE));
    }

}
