package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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
