package org.restaurant.voting.config;

import org.ehcache.expiry.ExpiryPolicy;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

public class ExpireCacheOnMidnight implements ExpiryPolicy<Object, Object> {
    @Override
    public Duration getExpiryForCreation(Object o, Object o2) {
        long msUntilMidnight = LocalTime.now().until(LocalTime.MAX, ChronoUnit.MILLIS);
        return Duration.of(msUntilMidnight, ChronoUnit.MILLIS);
    }

    @Override
    public Duration getExpiryForAccess(Object o, Supplier supplier) {
        return null;
    }

    @Override
    public Duration getExpiryForUpdate(Object o, Supplier supplier, Object o2) {
        return null;
    }
}
