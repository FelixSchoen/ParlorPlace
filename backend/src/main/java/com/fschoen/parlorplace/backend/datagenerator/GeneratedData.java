package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
public class GeneratedData {

    private UserCollection userCollection;
    private Map<User, String> passwordCollection;

    @Data
    public static class UserCollection {
        private User admin1;
        private User user1;
        private User user2;
        private User user3;

        private User neUser1; // non existent user
    }

}
