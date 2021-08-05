package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

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

        private User neUser1; // non existent user
    }

    @Data
    public static class WerewolfGameCollection {
        private WerewolfGame werewolfGame1;

        private Set<User> werewolfGame1Users;
    }

}
