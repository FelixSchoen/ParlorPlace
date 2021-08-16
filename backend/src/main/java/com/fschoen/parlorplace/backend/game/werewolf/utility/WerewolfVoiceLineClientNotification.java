package com.fschoen.parlorplace.backend.game.werewolf.utility;

import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoiceLineType;
import com.fschoen.parlorplace.backend.utility.communication.VoiceLineClientNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public class WerewolfVoiceLineClientNotification extends VoiceLineClientNotification {

    @NotNull
    private WerewolfVoiceLineType voiceLineType;

}
