package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.dto.user.*;
import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.unit.base.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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
