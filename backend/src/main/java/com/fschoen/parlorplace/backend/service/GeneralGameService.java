package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;

public interface GeneralGameService {

    Game<?, ?> getGameBaseInformation(GameIdentifier gameIdentifier);

}
