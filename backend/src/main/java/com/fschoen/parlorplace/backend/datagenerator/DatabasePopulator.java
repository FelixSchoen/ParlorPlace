package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Role;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfGamePhase;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Profile({"test", "setup"})
@Slf4j
public class DatabasePopulator {

    private final UserRepository userRepository;
    private final GameRepository<WerewolfGame> werewolfGameGameRepository;

    @Autowired
    public DatabasePopulator(
            UserRepository userRepository,
            GameRepository<WerewolfGame> werewolfGameGameRepository
    ) {
        this.userRepository = userRepository;
        this.werewolfGameGameRepository = werewolfGameGameRepository;
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
        generatedData.setWerewolfGameCollection(setupWerewolfGameCollection(generatedData));

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

    private GeneratedData.WerewolfGameCollection setupWerewolfGameCollection(GeneratedData generatedData) {
        GeneratedData.WerewolfGameCollection werewolfGameCollection = new GeneratedData.WerewolfGameCollection();

        //werewolfGame1
        WerewolfGame werewolfGame1 = WerewolfGame.builder()
                .gameIdentifier(new GameIdentifier("GAME1"))
                .gameState(GameState.LOBBY)
                .players(new HashSet<>())
                .ruleSet(new WerewolfRuleSet())
                .round(0)
                .startedAt(new Date())
                .gamePhase(WerewolfGamePhase.START_OF_ROUND)
                .build();
        werewolfGame1.getPlayers().add(
                WerewolfPlayer.builder()
                        .user(generatedData.getUserCollection().getAdmin1())
                        .game(werewolfGame1)
                        .lobbyRole(LobbyRole.ROLE_ADMIN)
                        .playerState(PlayerState.ALIVE)
                        .gameRoles(new ArrayList<>())
                        .position(0)
                        .disconnected(false)
                        .build()
        );
        werewolfGame1.getPlayers().add(
                WerewolfPlayer.builder()
                        .user(generatedData.getUserCollection().getUser1())
                        .game(werewolfGame1)
                        .lobbyRole(LobbyRole.ROLE_USER)
                        .playerState(PlayerState.ALIVE)
                        .gameRoles(new ArrayList<>())
                        .position(1)
                        .disconnected(false)
                        .build()
        );

        werewolfGameCollection.setWerewolfGame1(werewolfGameGameRepository.save(werewolfGame1));
        werewolfGameCollection.setWerewolfGame1Users(new HashSet<>(){{
            add(generatedData.getUserCollection().getAdmin1());
            add(generatedData.getUserCollection().getUser1());
        }});

        werewolfGameGameRepository.flush();
        return werewolfGameCollection;
    }

}
