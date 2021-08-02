package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.RoleMapperImpl;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapperImpl;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameRoleMapperImpl;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfPlayerMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfPlayerMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WerewolfPlayerMapperImpl.class, UserMapperImpl.class, RoleMapperImpl.class, WerewolfGameRoleMapperImpl.class})
public class WerewolfPlayerMapperTest {

    @Autowired
    private WerewolfPlayerMapper sut;

    @Test
    public void mapWerewolfPlayer_withNoObfuscation_toWerewolfPlayerDTO() {
        WerewolfGameRole gameRole = WerewolfGameRole.builder().id(0L).werewolfRoleType(WerewolfRoleType.VILLAGER).build();
        WerewolfPlayer player = WerewolfPlayer.builder().id(0L).playerState(PlayerState.ALIVE).position(0).gameRole(gameRole).build();
        WerewolfPlayerDTO dto = sut.toDTO(player);

        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getGameRoleDTO().getWerewolfRoleType()).isEqualTo(WerewolfRoleType.VILLAGER);
    }

}
