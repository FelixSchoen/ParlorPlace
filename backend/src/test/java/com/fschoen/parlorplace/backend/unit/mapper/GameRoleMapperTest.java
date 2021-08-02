package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapperImpl;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameRoleMapperImpl.class})
public class GameRoleMapperTest {

    @Autowired
    private GameRoleMapper sut;

    @Test
    public void mapWerewolfRole_toWerewolfRoleDTO() {
        WerewolfRole gameRole = WerewolfRole.builder().id(0L).werewolfRoleType(WerewolfRoleType.VILLAGER).build();
        WerewolfRoleDTO dto = sut.toDTO(gameRole);

        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getWerewolfRoleType()).isEqualTo(WerewolfRoleType.VILLAGER);
    }

}
