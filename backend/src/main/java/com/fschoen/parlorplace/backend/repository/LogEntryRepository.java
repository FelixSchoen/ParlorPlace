package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface LogEntryRepository<T extends LogEntry<?>> extends JpaRepository<T, Long> {

    /**
     * Finds a log entry by its id.
     *
     * @param id Id of the log entry
     * @return The found log entry
     */
    Optional<T> findOneById(Long id);

}
