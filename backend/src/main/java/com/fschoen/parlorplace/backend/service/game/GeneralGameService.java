package com.fschoen.parlorplace.backend.service.game;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;

public interface GeneralGameService {

    Game<?, ?, ?, ?> getActiveGameBaseInformation(GameIdentifier gameIdentifier);

    Game<?, ?, ?, ?> getIndividualGameBaseInformation(Long id);

}
