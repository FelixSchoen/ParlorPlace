package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.PlayerVoteCollection;
import com.fschoen.parlorplace.backend.entity.Vote;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.repository.VoteRepository;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.springframework.core.task.TaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractPlayerVoteService<
        V extends Vote<P, C>,
        G extends Game<P, ?, V, ?>,
        P extends Player<?>,
        C extends PlayerVoteCollection<P, P>,
        GRepo extends GameRepository<G>,
        VRepo extends VoteRepository<V>> extends AbstractVoteService<V, G, P, C, GRepo, VRepo> {

    public AbstractPlayerVoteService(CommunicationService communicationService, UserRepository userRepository, GRepo gameRepository, VRepo voteRepository, TaskExecutor taskExecutor) {
        super(communicationService, userRepository, gameRepository, voteRepository, taskExecutor);
    }

    // Utility

    public Map<P, C> getSameChoiceCollectionMap(Set<P> voters, Set<P> subjects, int amountVotes) {
        Map<P, C> map = new HashMap<>();
        for (P voter : voters) {
            C voteCollection;

            try {
                 voteCollection = this.getVoteCollectionClass().getDeclaredConstructor().newInstance();
                 voteCollection.setPlayer(voter);
                 voteCollection.setAmountVotes(amountVotes);
                 voteCollection.setSubjects(new HashSet<>(){{
                     addAll(subjects);
                 }});
                 voteCollection.setSelection(new HashSet<>());
                map.put(voter, voteCollection);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new DataConflictException(Messages.exception(MessageIdentifier.VOTE_TYPE_MISMATCH), e);
            }
        }
        return map;
    }

}
