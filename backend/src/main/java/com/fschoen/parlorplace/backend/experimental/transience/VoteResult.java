package com.fschoen.parlorplace.backend.experimental.transience;

import com.fschoen.parlorplace.backend.entity.*;
import lombok.*;

import java.util.*;

@Data
public class VoteResult<T> {

    /**
     * The selections the respective players made.
     */
    private Map<Player, Set<T>> selections;

}
