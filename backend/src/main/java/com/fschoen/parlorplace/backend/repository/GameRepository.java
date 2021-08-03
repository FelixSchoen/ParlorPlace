package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GameRepository<T extends Game<?, ?>> extends JpaRepository<T, Long> {

    /**
     * Finds a game by its id.
     *
     * @param id Id of the game
     * @return The found game
     */
    Optional<T> findOneById(Long id);

    /**
     * Finds all games matching the given identifier and end date.
     *
     * @param gameIdentifierToken Game identifier token of the games
     * @param endedAt        End date of the games
     * @return A list of all the found games
     */
    List<T> findAllByGameIdentifier_TokenAndEndedAt(String gameIdentifierToken, Data endedAt);

}
