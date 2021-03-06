package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GameRepository<T extends Game<?, ?, ?, ?>> extends JpaRepository<T, Long> {

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
     * @param endedAt             End date of the games
     * @return A list of all the found games
     */
    List<T> findAllByGameIdentifier_TokenAndEndedAt(String gameIdentifierToken, Date endedAt);

    /**
     * Finds all games matching the given end date.
     *
     * @param endedAt             End date of the games
     * @return A list of all the found games
     */
    List<T> findAllByEndedAt(Date endedAt);

    /**
     * Finds all games matching the given game state.
     *
     * @param gameState Game state of the games
     * @return A list of all the found games
     */
    List<T> findAllByGameState(GameState gameState);

    @Query("SELECT DISTINCT g FROM Game g JOIN g.players p WHERE p.user = :user")
    List<T> findAllByUserMember(@Param("user") User user);

}
