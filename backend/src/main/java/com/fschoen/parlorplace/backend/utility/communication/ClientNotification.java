package com.fschoen.parlorplace.backend.utility.communication;

import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Data
public class ClientNotification {

    @NotNull
    protected NotificationType notificationType;

}
