package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.persistent.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
