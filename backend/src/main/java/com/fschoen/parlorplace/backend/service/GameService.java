package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.game.management.GameIdentifier;

public interface GameService {

    GameIdentifier generateValidGameIdentifier();

}
