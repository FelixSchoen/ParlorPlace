package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RuleSetMapperImpl.class})
public class RuleSetMapperTest {

    @Autowired
    RuleSetMapper sut;

    @Test
    public void mapRuleSet_toRuleSetDTO() {
        WerewolfRuleSet werewolfRuleSet = WerewolfRuleSet.builder().id(0L).roles(new ArrayList<>(){{
            add(WerewolfRoleType.WEREWOLF);
            add(WerewolfRoleType.VILLAGER);
        }}).build();
        WerewolfRuleSetDTO dto = sut.toDTO(werewolfRuleSet, false);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getRoles().size()).isEqualTo(2);
    }

}
