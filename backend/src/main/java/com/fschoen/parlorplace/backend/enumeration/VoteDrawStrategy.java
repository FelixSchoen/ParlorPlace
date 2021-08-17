package com.fschoen.parlorplace.backend.enumeration;

public enum VoteDrawStrategy {
    /**
     * In case of a draw, choose a random option. Higher ranked alternatives will always be drawn before lower ranked ones.
     */
    CHOOSE_RANDOM,
    /**
     * If there exists any draw on any of the bins to select winners from, no winners will be selected at all.
     */
    HARD_NO_OUTCOME,
    /**
     * If there exists a draw on one of the bins, the winner calculation will stop at this point. Already determined definite winners will be returned.
     */
    SOFT_NO_OUTCOME
}
