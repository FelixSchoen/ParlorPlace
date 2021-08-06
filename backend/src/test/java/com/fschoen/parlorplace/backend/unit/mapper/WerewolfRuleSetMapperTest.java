package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfRuleSetMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfRuleSetMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WerewolfRuleSetMapperImpl.class})
public class WerewolfRuleSetMapperTest {

    @Autowired
    WerewolfRuleSetMapper sut;

    @Test
    public void mapWerewolfRuleSet_toWerewolfRuleSetDTO() {
        WerewolfRuleSet werewolfRuleSet = WerewolfRuleSet.builder().id(0L).gameRoleTypes(new ArrayList<>() {{
            add(WerewolfRoleType.WEREWOLF);
            add(WerewolfRoleType.VILLAGER);
        }}).build();
        WerewolfRuleSetDTO dto = sut.toDTO(werewolfRuleSet);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getGameRoleTypes().size()).isEqualTo(2);
    }

}
