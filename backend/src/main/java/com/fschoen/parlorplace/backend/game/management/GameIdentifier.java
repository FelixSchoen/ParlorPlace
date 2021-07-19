package com.fschoen.parlorplace.backend.game.management;

import lombok.Data;

import java.util.Random;

@Data
public class GameIdentifier {

    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String token;

    public GameIdentifier(int length) {
        this.generateToken(length);
    }

    public GameIdentifier(String token) {
        this.token = token;
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
