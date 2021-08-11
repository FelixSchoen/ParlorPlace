package com.fschoen.parlorplace.backend.controller.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class VoteCollectionDTO<P extends PlayerDTO<?>, T> {

    @NotNull
    protected P player;

    @NotNull
    protected Integer amountVotes;

    @NotNull
    protected Boolean allowAbstain;

    protected Boolean abstain;

    public abstract Set<T> getSubjects();

    public abstract void setSubjects(Set<T> subjects);

    public abstract Set<T> getSelection();

    public abstract void setSelection(Set<T> selection);

}
