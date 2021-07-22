package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapper;
import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapperImpl;
import com.fschoen.parlorplace.backend.controller.mapper.RoleMapperImpl;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapperImpl;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PlayerMapperImpl.class, UserMapperImpl.class, RoleMapperImpl.class})
public class PlayerMapperTest {

    @Autowired
    private PlayerMapper sut;

    @Test
    public void mapWerewolfPlayer_withObfuscation_toWerewolfPlayerDTO() {
        WerewolfRole gameRole = WerewolfRole.builder().id(0L).werewolfRoleType(WerewolfRoleType.VILLAGER).build();
        WerewolfPlayer player = WerewolfPlayer.builder().id(0L).playerState(PlayerState.ALIVE).position(0).werewolfRole(gameRole).build();
        WerewolfPlayerDTO dto = sut.toDTO(player, true);

        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getWerewolfRoleDTO()).isEqualTo(null);
    }

    @Test
    public void mapWerewolfPlayer_withNoObfuscation_toWerewolfPlayerDTO() {
        WerewolfRole gameRole = WerewolfRole.builder().id(0L).werewolfRoleType(WerewolfRoleType.VILLAGER).build();
        WerewolfPlayer player = WerewolfPlayer.builder().id(0L).playerState(PlayerState.ALIVE).position(0).werewolfRole(gameRole).build();
        WerewolfPlayerDTO dto = sut.toDTO(player, false);

        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getWerewolfRoleDTO().getWerewolfRoleType()).isEqualTo(WerewolfRoleType.VILLAGER);
    }

}
