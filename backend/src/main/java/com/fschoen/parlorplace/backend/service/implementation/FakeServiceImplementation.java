package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.service.FakeService;
import com.fschoen.parlorplace.backend.service.RandomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakeServiceImplementation implements FakeService {

    private final RandomService randomService;

     @Autowired
    public FakeServiceImplementation(RandomService randomService) {
        this.randomService = randomService;
    }

    public void fakeTimeND(long mean, long lowerBound, long upperBound) throws InterruptedException {
         this.fakeTimeND(mean, mean / 3, lowerBound, upperBound);
    }

    public void fakeTimeND(long mean, long stdDeviation, long lowerBound, long upperBound) throws InterruptedException {
        long normalDistributedRandom = (long) this.randomService.getRandomNDDouble(mean, stdDeviation);
        normalDistributedRandom = Math.max(lowerBound, normalDistributedRandom);
        normalDistributedRandom = Math.min(upperBound, normalDistributedRandom);

        Thread.sleep(normalDistributedRandom);
    }

}
