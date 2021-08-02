package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

@NoRepositoryBean
public interface RuleSetRepository<T extends RuleSet> extends JpaRepository<T, Long> {

    /**
     * Finds a rule set by its id.
     *
     * @param id Id of the rule set
     * @return The found rule set
     */
    Optional<T> findOneById(Long id);

}
