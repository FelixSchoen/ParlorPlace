package com.fschoen.parlorplace.backend.game.management;

import lombok.*;

import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
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

    public GameIdentifier(String token) {
        this.token = token.toUpperCase();
    }

    private void generateToken(int length) {
        Random random = new Random();
        StringBuilder tokenBuilder = new StringBuilder();

        while (tokenBuilder.length() < length) {
            int index = random.nextInt(CHARSET.length());
            tokenBuilder.append(CHARSET.charAt(index));
        }

        this.token = tokenBuilder.toString();
    }

}
