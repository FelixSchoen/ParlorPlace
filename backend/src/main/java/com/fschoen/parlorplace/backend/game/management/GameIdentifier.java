package com.fschoen.parlorplace.backend.game.management;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Data
public class GameIdentifier {

    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @NotNull
    @Size(min = 4)
    private String token;

    public GameIdentifier(int length) {
        this.generateToken(length);
    }

    private void generateToken(int length) {
        Random random = new Random();
        StringBuilder tokenBuilder = new StringBuilder();

        while (tokenBuilder.length() < length) {
            int index = (int) (random.nextFloat() * CHARSET.length());
            tokenBuilder.append(CHARSET.charAt(index));
        }

        this.token = tokenBuilder.toString();
    }

}
