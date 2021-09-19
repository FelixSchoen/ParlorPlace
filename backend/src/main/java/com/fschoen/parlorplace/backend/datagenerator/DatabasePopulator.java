package com.fschoen.parlorplace.backend.datagenerator;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.Role;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.enumeration.VoteDrawStrategy;
import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfLogEntry;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerVoteCollection;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayerWerewolfVote;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.VillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.WerewolfWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfGamePhase;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfLogType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.utility.other.ListBuilder;
import com.fschoen.parlorplace.backend.utility.other.SetBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Profile({"test", "setup"})
@Slf4j
public class DatabasePopulator {

    private final UserRepository userRepository;
    private final GameRepository<WerewolfGame> werewolfGameGameRepository;

    @Autowired
    private PlayerRepository<WerewolfPlayer> werewolfPlayerPlayerRepository;

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
        Set<Role> rolesAdmin1 = new SetBuilder<Role>()
                .add(Role.builder().role(UserRole.ROLE_USER).build())
                .add(Role.builder().role(UserRole.ROLE_ADMIN).build()).build();

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
        Set<Role> rolesUser1 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

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
        Set<Role> rolesUser2 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

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
        Set<Role> rolesUser3 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

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

        //user4
        Set<Role> rolesUser4 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User user4 = User.builder()
                .username("User4")
                .nickname("User4")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user4@mail.com")
                .roles(rolesUser4)
                .build();
        user4.getRoles().forEach(role -> role.setUser(user4));

        userCollection.setUser4(userRepository.save(user4));
        passwordCollection.put(user4, "password");

        //user5
        Set<Role> rolesUser5 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User user5 = User.builder()
                .username("User5")
                .nickname("User5")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user5@mail.com")
                .roles(rolesUser5)
                .build();
        user5.getRoles().forEach(role -> role.setUser(user5));

        userCollection.setUser5(userRepository.save(user5));
        passwordCollection.put(user5, "password");

        //user6
        Set<Role> rolesUser6 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User user6 = User.builder()
                .username("User6")
                .nickname("User6")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user6@mail.com")
                .roles(rolesUser6)
                .build();
        user6.getRoles().forEach(role -> role.setUser(user6));

        userCollection.setUser6(userRepository.save(user6));
        passwordCollection.put(user6, "password");

        //user7
        Set<Role> rolesUser7 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User user7 = User.builder()
                .username("User7")
                .nickname("User7")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user7@mail.com")
                .roles(rolesUser7)
                .build();
        user7.getRoles().forEach(role -> role.setUser(user7));

        userCollection.setUser7(userRepository.save(user7));
        passwordCollection.put(user7, "password");

        //user8
        Set<Role> rolesUser8 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User user8 = User.builder()
                .username("User8")
                .nickname("User8")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user8@mail.com")
                .roles(rolesUser8)
                .build();
        user8.getRoles().forEach(role -> role.setUser(user8));

        userCollection.setUser8(userRepository.save(user8));
        passwordCollection.put(user8, "password");

        //user9
        Set<Role> rolesUser9 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User user9 = User.builder()
                .username("User9")
                .nickname("User9")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user9@mail.com")
                .roles(rolesUser9)
                .build();
        user9.getRoles().forEach(role -> role.setUser(user9));

        userCollection.setUser9(userRepository.save(user9));
        passwordCollection.put(user9, "password");

        //user10
        Set<Role> rolesUser10 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User user10 = User.builder()
                .username("User10")
                .nickname("User10")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("user10@mail.com")
                .roles(rolesUser10)
                .build();
        user10.getRoles().forEach(role -> role.setUser(user10));

        userCollection.setUser10(userRepository.save(user10));
        passwordCollection.put(user10, "password");

        //non existant user1
        Set<Role> rolesNeUser1 = new SetBuilder<Role>().add(Role.builder().role(UserRole.ROLE_USER).build()).build();

        User neUser1 = User.builder()
                .username("nonExistentUsername")
                .nickname("nonExistentNickname")
                .password("$2a$10$G7E1tKKajd3S8/ORM17isOTCH0To0VkAnTjY7R4gkcgNpyLG/.tJC")
                .email("neUser1@mail.com")
                .roles(rolesNeUser1)
                .build();
        neUser1.getRoles().forEach(role -> role.setUser(neUser1));

        userCollection.setNeUser1(neUser1);
        passwordCollection.put(neUser1, "password");

        userRepository.flush();
        return userCollection;
    }

    private GeneratedData.WerewolfGameCollection setupWerewolfGameCollection(GeneratedData generatedData) {
        GeneratedData.WerewolfGameCollection werewolfGameCollection = new GeneratedData.WerewolfGameCollection();

        //werewolfLobbyGame1
        {
            WerewolfGame werewolfLobbyGame1 = WerewolfGame.builder()
                    .gameIdentifier(new GameIdentifier("LOBBY1"))
                    .gameState(GameState.LOBBY)
                    .players(new HashSet<>())
                    .ruleSet(WerewolfRuleSet.builder()
                            .resourcePack("DEFAULT")
                            .gameRoleTypes(new ListBuilder<WerewolfRoleType>()
                                    .add(WerewolfRoleType.VILLAGER)
                                    .add(WerewolfRoleType.PURE_VILLAGER)
                                    .add(WerewolfRoleType.WEREWOLF)
                                    .add(WerewolfRoleType.SEER)
                                    .add(WerewolfRoleType.WITCH)
                                    .add(WerewolfRoleType.HUNTER)
                                    .add(WerewolfRoleType.CUPID)
                                    .add(WerewolfRoleType.BODYGUARD)
                                    .add(WerewolfRoleType.LYCANTHROPE)
                                    .add(WerewolfRoleType.BEAR_TAMER).build()
                            ).build())
                    .round(0)
                    .votes(new ArrayList<>())
                    .log(new ArrayList<>())
                    .startedAt(Instant.now())
                    .gamePhase(WerewolfGamePhase.START_OF_ROUND)
                    .build();
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getAdmin1())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_ADMIN)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(0)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser1())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(1)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser2())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(2)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser3())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(3)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser4())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(4)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser5())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(5)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser6())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(6)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser7())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(7)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser8())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(8)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser9())
                            .game(werewolfLobbyGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(9)
                            .disconnected(false)
                            .build()
            );

            werewolfGameCollection.setWerewolfLobbyGame1(werewolfGameGameRepository.save(werewolfLobbyGame1));
            werewolfGameCollection.setWerewolfLobbyGame1Users(werewolfLobbyGame1.getPlayers().stream().map(Player::getUser).collect(Collectors.toList()));
        }

        //werewolfLobbyGame2
        {
            WerewolfGame werewolfLobbyGame2 = WerewolfGame.builder()
                    .gameIdentifier(new GameIdentifier("LOBBY2"))
                    .gameState(GameState.LOBBY)
                    .players(new HashSet<>())
                    .ruleSet(WerewolfRuleSet.builder()
                            .resourcePack("DEFAULT")
                            .gameRoleTypes(new ListBuilder<WerewolfRoleType>()
                                    .add(WerewolfRoleType.WEREWOLF)
                                    .add(WerewolfRoleType.SEER)
                                    .add(WerewolfRoleType.WITCH)
                                    .add(WerewolfRoleType.BODYGUARD).build()
                            ).build())
                    .round(0)
                    .votes(new ArrayList<>())
                    .log(new ArrayList<>())
                    .startedAt(Instant.now())
                    .gamePhase(WerewolfGamePhase.START_OF_ROUND)
                    .build();
            werewolfLobbyGame2.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getAdmin1())
                            .game(werewolfLobbyGame2)
                            .lobbyRole(LobbyRole.ROLE_ADMIN)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(0)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame2.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser1())
                            .game(werewolfLobbyGame2)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(1)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame2.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser2())
                            .game(werewolfLobbyGame2)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(2)
                            .disconnected(false)
                            .build()
            );
            werewolfLobbyGame2.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser3())
                            .game(werewolfLobbyGame2)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ArrayList<>())
                            .position(3)
                            .disconnected(false)
                            .build()
            );

            werewolfGameCollection.setWerewolfLobbyGame2(werewolfGameGameRepository.save(werewolfLobbyGame2));
            werewolfGameCollection.setWerewolfLobbyGame2Users(werewolfLobbyGame2.getPlayers().stream().map(Player::getUser).collect(Collectors.toList()));
        }

        //werewolfOngoingGame1
        {
            WerewolfGame werewolfOngoingGame1 = WerewolfGame.builder()
                    .gameIdentifier(new GameIdentifier("ONGOING1"))
                    .gameState(GameState.ONGOING)
                    .players(new HashSet<>())
                    .ruleSet(WerewolfRuleSet.builder()
                            .resourcePack("DEFAULT")
                            .gameRoleTypes(new ListBuilder<WerewolfRoleType>()
                                    .add(WerewolfRoleType.VILLAGER)
                                    .add(WerewolfRoleType.VILLAGER)
                                    .add(WerewolfRoleType.WEREWOLF).build()).build())
                    .round(0)
                    .votes(new ArrayList<>())
                    .log(new ArrayList<>())
                    .startedAt(Instant.now())
                    .gamePhase(WerewolfGamePhase.START_OF_ROUND)
                    .build();
            werewolfOngoingGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getAdmin1())
                            .game(werewolfOngoingGame1)
                            .lobbyRole(LobbyRole.ROLE_ADMIN)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ListBuilder<WerewolfGameRole>().add(new WerewolfWerewolfGameRole()).build())
                            .position(0)
                            .disconnected(false)
                            .build()
            );
            werewolfOngoingGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser1())
                            .game(werewolfOngoingGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ListBuilder<WerewolfGameRole>().add(new VillagerWerewolfGameRole()).build())
                            .position(1)
                            .disconnected(false)
                            .build()
            );
            werewolfOngoingGame1.getPlayers().add(
                    WerewolfPlayer.builder()
                            .user(generatedData.getUserCollection().getUser2())
                            .game(werewolfOngoingGame1)
                            .lobbyRole(LobbyRole.ROLE_USER)
                            .playerState(PlayerState.ALIVE)
                            .gameRoles(new ListBuilder<WerewolfGameRole>().add(new VillagerWerewolfGameRole()).build())
                            .position(2)
                            .disconnected(false)
                            .build()
            );
            for (WerewolfPlayer p : werewolfOngoingGame1.getPlayers()) {
                p.getGameRoles().forEach(werewolfGameRole -> werewolfGameRole.setPlayer(p));
            }
            werewolfOngoingGame1.getVotes().add(
                    WerewolfPlayerWerewolfVote.builder()
                            .game(werewolfOngoingGame1)
                            .voteState(VoteState.ONGOING)
                            .voteType(VoteType.PUBLIC_PUBLIC_PUBLIC)
                            .voteDrawStrategy(VoteDrawStrategy.CHOOSE_RANDOM)
                            .voteDescriptor(WerewolfVoteDescriptor.WEREWOLVES_KILL)
                            .voters(new HashSet<>())
                            .round(1)
                            .endTime(Instant.now().plusSeconds(600))
                            .voteCollectionMap(new HashMap<>())
                            .outcome(new HashSet<>())
                            .outcomeAmount(1)
                            .build()
            );
            werewolfOngoingGame1.getLog().add(
                    WerewolfLogEntry.builder()
                            .identifier(UUID.randomUUID())
                            .game(werewolfOngoingGame1)
                            .recipients(werewolfOngoingGame1.getPlayers())
                            .logType(WerewolfLogType.START).build());

            werewolfGameGameRepository.save(werewolfOngoingGame1);

            for (WerewolfPlayer p : werewolfOngoingGame1.getPlayers()) {
                werewolfOngoingGame1.getVotes().get(0).getVoters().add(p);
                ((WerewolfPlayerWerewolfVote) werewolfOngoingGame1.getVotes().get(0)).getVoteCollectionMap().put(p.getId(),
                        WerewolfPlayerVoteCollection.builder()
                                .amountVotes(1)
                                .allowAbstain(false)
                                .subjects(new SetBuilder<WerewolfPlayer>().add(p).build())
                                .selection(new HashSet<>())
                                .build());
            }

            werewolfGameCollection.setWerewolfOngoingGame1(werewolfGameGameRepository.save(werewolfOngoingGame1));
            werewolfGameCollection.setWerewolfOngoingGame1Users(werewolfOngoingGame1.getPlayers().stream().map(Player::getUser).collect(Collectors.toList()));
        }

        werewolfGameGameRepository.flush();
        return werewolfGameCollection;
    }

}
