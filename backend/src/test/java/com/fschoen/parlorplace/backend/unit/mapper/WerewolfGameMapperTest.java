package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameIdentifierMapperImpl;
import com.fschoen.parlorplace.backend.controller.mapper.RoleMapperImpl;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapperImpl;
import com.fschoen.parlorplace.backend.entity.Role;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameMapperImpl;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameRoleMapperImpl;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfPlayerMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WerewolfGameMapperImpl.class, WerewolfPlayerMapperImpl.class, WerewolfGameRoleMapperImpl.class, UserMapperImpl.class, RoleMapperImpl.class, GameIdentifierMapperImpl.class})
public class WerewolfGameMapperTest {

    @Autowired
    WerewolfGameMapper sut;

    @Test
    public void mapWerewolfGame_toWerewolfGameDTO() {
        User user = User.builder().roles(new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }}).build();
        WerewolfGameRole gameRole = WerewolfGameRole.builder().id(0L).werewolfRoleType(WerewolfRoleType.VILLAGER).build();
        WerewolfPlayer player = WerewolfPlayer.builder().id(0L).user(user).playerState(PlayerState.ALIVE).position(0).gameRoles(new ArrayList<>(){{
            add(gameRole);
        }}).build();
        Set<WerewolfPlayer> players = new HashSet<>() {{
            add(player);
        }};
        WerewolfGame game = WerewolfGame.builder().id(0L).players(players).build();
        WerewolfGameDTO dto = sut.toDTO(game);

        assertThat(dto.getGameType()).isEqualTo(GameType.WEREWOLF);
        assertThat(dto.getPlayers().iterator().next().getUser().getUserRoles().size()).isEqualTo(1);
        assertThat(dto.getPlayers().iterator().next().getId()).isEqualTo(0);
        assertThat(dto.getPlayers().iterator().next().getPlayerState()).isEqualTo(PlayerState.ALIVE);
    }

}
