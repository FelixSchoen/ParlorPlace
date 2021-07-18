package com.fschoen.parlorplace.backend.entity.transience;

import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
public class VoteRequest<T> {

    /**
     * The players that participate in the vote.
     */
    private Set<Player> voters;

    /**
     * The objects the players have to vote on.
     */
    private Map<Player, Set<T>> subjects;

    /**
     * The amount of votes each player can cast.
     */
    private Map<Player, Integer> amountVotes;

    /**
     * Type of the vote, see {@link VoteType} for more explanation.
     */
    private VoteType voteType;

    /**
     * End time of the vote.
     */
    private Date endTime;

    /**
     * Duration of the grace period in seconds which determines how long players are allowed to change their selections
     * after the final selection needed to end the vote has been cast. Does not extend over the overall duration given
     * by {@link #endTime}.
     */
    private Integer durationGracePeriod;

}
