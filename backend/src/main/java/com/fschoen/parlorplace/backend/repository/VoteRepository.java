package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface VoteRepository<T extends Vote<?, ?, ?, ?>> extends JpaRepository<T, Long> {

    /**
     * Finds a vote by its id.
     *
     * @param id Id of the vote
     * @return The found vote
     */
    Optional<T> findOneById(Long id);

    List<T> findAllByGame_GameIdentifier_Token(String gameIdentifierToken);

}
