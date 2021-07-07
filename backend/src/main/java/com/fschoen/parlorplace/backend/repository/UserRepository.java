package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.persistance.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by its id.
     * @param id Id of the user
     * @return The found user
     */
    Optional<User> findOneById(Long id);

    /**
     * Finds a user by its username.
     * @param username Username of the user
     * @return The found user
     */
    Optional<User> findOneByUsername(String username);

    /**
     * Finds a user by its email.
     * @param email Email of the user
     * @return The found user
     */
    Optional<User> findOneByEmail(String email);

}
