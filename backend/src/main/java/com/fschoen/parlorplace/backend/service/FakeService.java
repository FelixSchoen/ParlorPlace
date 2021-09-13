package com.fschoen.parlorplace.backend.service;

public interface FakeService {

    /**
     * Calls {@link FakeService#fakeTimeND(long, long, long, long)} with stdDeviation as {@param mean}/3.
     * @param mean Mean of the normal distribution
     * @param lowerBound Lower bound to scale the wait time to
     * @param upperBound Upper bound to scale the wait time to
     * @throws InterruptedException If the thread is interrupted while sleeping
     */
    void fakeTimeND(long mean, long lowerBound, long upperBound) throws InterruptedException;

    /**
     * Waits a random amount of time. This random amount is drawn according to a normal distribution, given by the parameters.
     * @param mean Mean of the normal distribution
     * @param stdDeviation Standard deviation of the normal distribution
     * @param lowerBound Lower bound to scale the wait time to
     * @param upperBound Upper bound to scale the wait time to
     * @throws InterruptedException If the thread is interrupted while sleeping
     */
    void fakeTimeND(long mean, long stdDeviation, long lowerBound, long upperBound) throws InterruptedException;

}
