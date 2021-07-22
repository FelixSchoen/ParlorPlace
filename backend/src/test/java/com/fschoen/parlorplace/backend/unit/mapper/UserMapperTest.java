package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.dto.user.UserUpdateRequestDTO;
import com.fschoen.parlorplace.backend.controller.mapper.RoleMapperImpl;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapperImpl;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.unit.base.BaseUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserMapperImpl.class, RoleMapperImpl.class})
public class UserMapperTest extends BaseUnitTest {

    @Autowired
    private UserMapper sut;

    @Test
    public void mapUpdateRequest_toUser() {
        UserUpdateRequestDTO userUpdateRequestDTO = UserUpdateRequestDTO.builder()
                .id(1L)
                .username("username")
                .nickname("nickname")
                .password("password")
                .email("email")
                .roles(new HashSet<>() {{
                    add(UserRole.ROLE_USER);
                }})
                .build();
        User user = sut.fromDTO(userUpdateRequestDTO);

        assertThat(user.getRoles()).isNotNull();
        assertThat(user.getRoles().size()).isEqualTo(1);
    }

}
