package com.fschoen.parlorplace.backend.entity.transience;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@AllArgsConstructor
@Data
public class GameIdentifier {

    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

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
