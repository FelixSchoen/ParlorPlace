package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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
