package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its id.
     *
     * @param id Id of the role
     *
     * @return The found role
     */
    Optional<Role> findOneById(Long id);

    /**
     * Deletes all roles that belong to the user of the given id.
     *
     * @param userId Id of the user to delete roles for
     */
    void removeByUserId(Long userId);

}
