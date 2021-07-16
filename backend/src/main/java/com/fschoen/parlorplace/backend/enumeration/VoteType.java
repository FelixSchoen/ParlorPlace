package com.fschoen.parlorplace.backend.enumeration;

public enum VoteType {
    /**
     * Everyone knows about the vote, about the voters, about the votes
     */
    PUBLIC_PUBLIC_PUBLIC,
    /**
     * Everyone knows about the vote, about the voters, votes are revealed after the vote
     */
    PUBLIC_PUBLIC_REVEAL,
    /**
     * Everyone knows about the vote, about the voters, not about the votes
     */
    PUBLIC_PUBLIC_PRIVATE,
    /**
     * Only voters know about the vote, they know about the voters, about the votes
     */
    PRIVATE_PUBLIC_PUBLIC
}
