package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.service.RandomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class RandomServiceImplementation implements RandomService {

    private final Random random;

    public RandomServiceImplementation() {
        this.random = new Random();
    }

    public RandomServiceImplementation(long seed) {
        this.random = new Random(seed);
    }

    public int getRandomInt() {
        return this.random.nextInt();
    }

    public double getRandomGaussian() {
        return this.random.nextGaussian();
    }

    public double getRandomNDDouble(double mean, double stdDeviation) {
        return this.random.nextGaussian()*stdDeviation+mean;
    }

}
