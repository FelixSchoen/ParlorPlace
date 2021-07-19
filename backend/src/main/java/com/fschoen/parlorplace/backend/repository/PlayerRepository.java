package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.persistance.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface PlayerRepository<T extends Player> extends JpaRepository<T, Long> {

    /**
     * Finds a player by its id.
     *
     * @param id Id of the player
     * @return The found player
     */
    Optional<T> findOneById(Long id);

}
