package com.fschoen.parlorplace.backend.experimental.transience;

import com.fschoen.parlorplace.backend.entity.Player;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class VoteResult<T> {

    /**
     * The selections the respective players made.
     */
    private Map<Player, Set<T>> selections;

}
