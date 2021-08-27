package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class GeneratedData {

    private UserCollection userCollection;
    private Map<User, String> passwordCollection;

    private WerewolfGameCollection werewolfGameCollection;

    @Data
    public static class UserCollection {
        private User admin1;
        private User user1;
        private User user2;
        private User user3;
        private User user4;

        private User neUser1; // non existent user
    }

    @Data
    public static class WerewolfGameCollection {
        private WerewolfGame werewolfLobbyGame1;

        private List<User> werewolfLobbyGame1Users;

        private WerewolfGame werewolfOngoingGame1;

        private List<User> werewolfOngoingGame1Users;
    }

}
