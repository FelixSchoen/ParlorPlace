package com.fschoen.parlorplace.backend.utility.communication;

import com.fschoen.parlorplace.backend.enumeration.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Data
public class ClientNotification {

    @NotNull
    NotificationType notificationType;

}
