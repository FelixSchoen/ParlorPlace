package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole.WerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole.VillagerWerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameRoleMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameRoleMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WerewolfGameRoleMapperImpl.class})
public class WerewolfGameRoleMapperTest {

    @Autowired
    private WerewolfGameRoleMapper sut;

    @Test
    public void mapWerewolfGameRole_toWerewolfGameRoleDTO() {
        WerewolfGameRole gameRole = VillagerWerewolfGameRole.builder().id(0L).build();
        WerewolfGameRoleDTO dto = sut.toDTO(gameRole);

        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getWerewolfRoleType()).isEqualTo(WerewolfRoleType.VILLAGER);
    }

}
