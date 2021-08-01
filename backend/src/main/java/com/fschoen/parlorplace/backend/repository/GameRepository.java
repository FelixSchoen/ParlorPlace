package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.persistance.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

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
