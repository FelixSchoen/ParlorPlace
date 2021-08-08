package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.Vote;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractVoteService<
        V extends Vote> {

    public CompletableFuture<V> requestVote() {
        return null;
    }

}
