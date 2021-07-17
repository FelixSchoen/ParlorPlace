package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.utility.obfuscation.Obfuscateable;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class PlayerDTO implements Obfuscateable {

    @NotNull
    private Long id;

    @NotNull
    private UserDTO user;

    @NotNull
    @Min(0)
    private Integer position;

    @Override
    public void obfuscate() {
        this.user.obfuscate();
    }

}
