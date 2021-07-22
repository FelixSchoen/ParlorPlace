package com.fschoen.parlorplace.backend.game.management;

import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.transience.VoteRequest;
import com.fschoen.parlorplace.backend.entity.transience.VoteResult;
import com.fschoen.parlorplace.backend.utility.concurrency.CPromise;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.*;

public abstract class GameModerator {

    @Setter(value= AccessLevel.PUBLIC)
    protected GameManager gameManager;

    protected Map<VoteRequest<Object>, CPromise<Set<VoteResult<Object>>, VoteResult<Object>>> activeVoteRequests = Collections.synchronizedMap(new HashMap<>());

    public <T> CPromise<Set<VoteResult<T>>, VoteResult<T>> requestVote(Set<VoteRequest<T>> voteRequestSet) {
        CPromise<Set<VoteResult<T>>, VoteResult<T>> promise = new CPromise<>(voteRequestSet.size());
        promise.write(new HashSet<>());



        return promise;
    }

    public Set<Player> getPlayers() {
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
