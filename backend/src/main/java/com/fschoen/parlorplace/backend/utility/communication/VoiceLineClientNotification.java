package com.fschoen.parlorplace.backend.utility.communication;

import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public abstract class VoiceLineClientNotification extends ClientNotification {

    @NotNull
    @Builder.Default
    protected NotificationType notificationType = NotificationType.VOICE_LINE;

    @NotNull
    private Set<CodeName> codeNames;

}
