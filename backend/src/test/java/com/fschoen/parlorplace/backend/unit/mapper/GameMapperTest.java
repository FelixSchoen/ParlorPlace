package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameMapperImpl.class, PlayerMapperImpl.class, UserMapperImpl.class, RoleMapperImpl.class, GameIdentifierMapperImpl.class})
public class GameMapperTest {

    @Autowired
    GameMapper sut;

    @Test
    public void mapWerewolfGame_toWerewolfGameDTO() {
        WerewolfRole gameRole = WerewolfRole.builder().id(0L).werewolfRoleType(WerewolfRoleType.VILLAGER).build();
        WerewolfPlayer player = WerewolfPlayer.builder().id(0L).playerState(PlayerState.ALIVE).position(0).werewolfRole(gameRole).build();
        Set<WerewolfPlayer> players = new HashSet<>(){{
            add(player);
        }};
        WerewolfGame game = WerewolfGame.builder().id(0L).players(players).build();
        WerewolfGameDTO dto = sut.toDTO(game, false);

        assertThat(dto.getGameType()).isEqualTo(GameType.WEREWOLF);
        assertThat(dto.getPlayers().iterator().next().getId()).isEqualTo(0);
        assertThat(dto.getPlayers().iterator().next().getPlayerState()).isEqualTo(PlayerState.ALIVE);
    }

}
