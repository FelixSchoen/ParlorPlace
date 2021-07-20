package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Profile({"test", "setup"})
@Slf4j
public class DatabasePopulator {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public DatabasePopulator(UserRepository userRepository, GameRepository gameRepository, PlayerRepository playerRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Creates and persists test data to be used by the integration tests.
     *
     * @return An object of the type {@link GeneratedData} containing links to the data
     */
    public GeneratedData generate() {
        GeneratedData generatedData = new GeneratedData();

        Map<User, String> passwordCollection = new HashMap<>();
        generatedData.setPasswordCollection(passwordCollection);

        log.info("Generating UserCollection");
        generatedData.setUserCollection(setupUserCollection(passwordCollection));

        log.info("Generating WerewolfGameCollection");
        generatedData.setWerewolfGameCollection(setupWerewolfGameCollection());

        log.info("Generating WerewolfPlayerCollection");
        generatedData.setWerewolfPlayerCollection(setupWerewolfPlayerCollection(generatedData.getUserCollection(), generatedData.getWerewolfGameCollection()));

        return generatedData;
    }

    private GeneratedData.UserCollection setupUserCollection(Map<User, String> passwordCollection) {
        GeneratedData.UserCollection userCollection = new GeneratedData.UserCollection();

        //admin1
        Set<Role> rolesAdmin1 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
            add(Role.builder().role(UserRole.ROLE_ADMIN).build());
        }};
        User admin1 = User.builder()
                .username("Admin1")
                .nickname("Admin1")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("admin1@mail.com")
                .roles(rolesAdmin1)
                .build();
        admin1.getRoles().forEach(role -> role.setUser(admin1));

        userCollection.setAdmin1(userRepository.save(admin1));
        passwordCollection.put(admin1, "password");

        //user1
        Set<Role> rolesUser1 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};
        User user1 = User.builder()
                .username("User1")
                .nickname("User1")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user1@mail.com")
                .roles(rolesUser1)
                .build();
        user1.getRoles().forEach(role -> role.setUser(user1));

        userCollection.setUser1(userRepository.save(user1));
        passwordCollection.put(user1, "password");

        //user2
        Set<Role> rolesUser2 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};
        User user2 = User.builder()
                .username("User2")
                .nickname("User2")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user2@mail.com")
                .roles(rolesUser2)
                .build();
        user2.getRoles().forEach(role -> role.setUser(user2));

        userCollection.setUser2(userRepository.save(user2));
        passwordCollection.put(user2, "password");

        //user3
        Set<Role> rolesUser3 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};
        User user3 = User.builder()
                .username("User3")
                .nickname("User3")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user3@mail.com")
                .roles(rolesUser3)
                .build();
        user3.getRoles().forEach(role -> role.setUser(user3));

        userCollection.setUser3(userRepository.save(user3));
        passwordCollection.put(user3, "password");

        //non existant user1
        Set<Role> rolesneUser1 = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};
        User neUser1 = User.builder()
                .username("nonExistentUsername")
                .nickname("nonExistentNickname")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("neUser1@mail.com")
                .roles(rolesneUser1)
                .build();
        neUser1.getRoles().forEach(role -> role.setUser(neUser1));

        userCollection.setNeUser1(neUser1);
        passwordCollection.put(neUser1, "password");

        userRepository.flush();
        return userCollection;
    }

    private GeneratedData.WerewolfGameCollection setupWerewolfGameCollection() {
        GeneratedData.WerewolfGameCollection werewolfGameCollection = new GeneratedData.WerewolfGameCollection();

        // Werewolf Game 1
        WerewolfGame werewolfGame1 = WerewolfGame.builder()
                .startedAt(new Date())
                .gameIdentifier(new GameIdentifier("GAME1"))
                .players(new HashSet<>())
                .ruleSet(new WerewolfRuleSet())
                .build();

        werewolfGameCollection.setWerewolfGame1((WerewolfGame) gameRepository.save(werewolfGame1));

        return werewolfGameCollection;
    }

    private GeneratedData.WerewolfPlayerCollection setupWerewolfPlayerCollection(GeneratedData.UserCollection userCollection, GeneratedData.WerewolfGameCollection werewolfGameCollection) {
        GeneratedData.WerewolfPlayerCollection werewolfPlayerCollection = new GeneratedData.WerewolfPlayerCollection();

        // Werewolf Player 1
        WerewolfPlayer werewolfPlayer1 = WerewolfPlayer.builder()
                .user(userCollection.getUser1())
                .lobbyRole(LobbyRole.ROLE_ADMIN)
                .playerState(PlayerState.ALIVE)
                .position(0)
                .game(werewolfGameCollection.getWerewolfGame1())
                .werewolfRole(null)
                .build();
        werewolfPlayerCollection.setWerewolfPlayer1((WerewolfPlayer) playerRepository.save(werewolfPlayer1));

        return werewolfPlayerCollection;
    }

}
