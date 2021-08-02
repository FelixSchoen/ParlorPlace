package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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
