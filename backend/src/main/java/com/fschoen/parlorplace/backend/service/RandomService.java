package com.fschoen.parlorplace.backend.service;

public interface RandomService {

    /**
     * Returns a random integer.
     * @return A random integer
     */
    int getRandomInt();

    /**
     * Returns a random double according to a normal distribution with mean=0 and stdDev=1.
     * @return A random double
     */
    double getRandomGaussian();

    /**
     * Returns a random double according to a normal distribution with the given parameters.
     * @param mean The mean of the normal distribution
     * @param stdDeviation The standard deviation of the normal distribution
     * @return The random number from the normal distribution
     */
    double getRandomNDDouble(double mean, double stdDeviation);

}
