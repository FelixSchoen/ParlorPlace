package com.fschoen.parlorplace.backend.unit.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.RoleMapper;
import com.fschoen.parlorplace.backend.controller.mapper.RoleMapperImpl;
import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.unit.base.BaseUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RoleMapperImpl.class})
public class RoleMapperTest extends BaseUnitTest {

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

    @Test
    public void mapRole_toUserRole() {
        Role actualRole = Role.builder().id(1L).role(UserRole.ROLE_USER).build();
        UserRole actualUserRole = sut.toUserRole(actualRole);

        assertEquals(UserRole.ROLE_USER, actualUserRole);
    }

}
