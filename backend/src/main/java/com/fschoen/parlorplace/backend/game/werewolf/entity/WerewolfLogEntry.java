package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.LogEntry;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfLogType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfLogEntry extends LogEntry<WerewolfPlayer> {

    @Column(nullable = false)
    @Enumerated
    @NotNull
    private WerewolfLogType logType;

}
