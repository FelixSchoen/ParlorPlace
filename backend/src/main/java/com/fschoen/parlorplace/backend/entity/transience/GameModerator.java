package com.fschoen.parlorplace.backend.entity.transience;

import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.enumeration.VoteType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameModerator {

    public void vote(List<Player> voters, Map<Player, List<Object>> subjects, Map<Player, Integer> amountVotes, VoteType voteType) {

    }

    public List<Player> getPlayers() {
        return null;
    }

    public <T> Map<Player, T> forAllSame(T t) {
        Map<Player, T> map = new HashMap<>();

        for (Player p : this.getPlayers()) {
            map.put(p, t);
        }

        return map;
    }

}
