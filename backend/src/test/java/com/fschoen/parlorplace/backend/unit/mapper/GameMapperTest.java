package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameMapperImpl.class, PlayerMapperImpl.class, UserMapperImpl.class, RoleMapperImpl.class})
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
