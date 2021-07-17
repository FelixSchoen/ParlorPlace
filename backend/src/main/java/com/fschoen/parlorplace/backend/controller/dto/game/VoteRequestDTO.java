package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.enumeration.VoteType;
import com.fschoen.parlorplace.backend.utility.obfuscation.Obfuscateable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class VoteRequestDTO<T> implements Obfuscateable {

    @NotNull
    private Set<PlayerDTO> voters;

    @NotNull
    private Map<PlayerDTO, Set<T>> subjects;

    @NotNull
    private Map<PlayerDTO, Integer> amountVotes;

    @NotNull
    private VoteType voteType;

    @NotNull
    private Date endTime;

    @Override
    public void obfuscate() {
        this.voters.forEach(PlayerDTO::obfuscate);
        this.subjects.forEach((key, value) -> {
            key.obfuscate();
            if (value instanceof Obfuscateable) ((Obfuscateable) value).obfuscate();
        });
        this.amountVotes.forEach((key, value) -> key.obfuscate());
    }

}
