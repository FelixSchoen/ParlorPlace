package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.user.RoleMapper;
import com.fschoen.parlorplace.backend.controller.mapper.user.RoleMapperImpl;
import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.enums.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RoleMapperImpl.class})
public class RoleMapperTest {

    @Autowired
    private RoleMapper sut;

    @Test
    public void mapString_toRole() {
        String roleMoniker = "ROLE_USER";
        Role actualRole = sut.toRole(roleMoniker);
        Role expectedRole = Role.builder().role(UserRole.ROLE_USER).build();

        assertEquals(expectedRole, actualRole);
    }

    @Test
    public void mapSetOfStrings_toSetOfRoles() {
        String roleMoniker = "ROLE_USER";
        Set<String> stringSet = new HashSet<>(){{
            add(roleMoniker);
        }};

        Set<Role> actualSet = sut.toRole(stringSet);
        Role expectedRole = Role.builder().role(UserRole.ROLE_USER).build();
        Set<Role> expectedSet = new HashSet<>(){{
            add(expectedRole);
        }};

        assertEquals(expectedSet, actualSet);
    }

}
