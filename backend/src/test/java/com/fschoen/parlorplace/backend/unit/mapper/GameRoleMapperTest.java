package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapper;
import com.fschoen.parlorplace.backend.controller.mapper.GameRoleMapperImpl;
import com.fschoen.parlorplace.backend.entity.persistance.GameRole;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfRoleMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfRoleMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GameRoleMapperImpl.class, WerewolfRoleMapperImpl.class})
public class GameRoleMapperTest {

    @Autowired
    private WerewolfRoleMapper sut_werewolf;

    @Test
    public void mapWerewolfRole_toWerewolfRoleDTO() {
        WerewolfRole gameRole = WerewolfRole.builder().id(0L).werewolfRoleType(WerewolfRoleType.VILLAGER).build();
        WerewolfRoleDTO dto = sut_werewolf.toDTO(gameRole);
    }

}
