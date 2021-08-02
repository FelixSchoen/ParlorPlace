package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface GameRepository<T extends Game> extends JpaRepository<T, Long> {

    /**
     * Finds a game by its id.
     *
     * @param id Id of the game
     * @return The found game
     */
    Optional<T> findOneById(Long id);

}
