package org.restaurant.voting.config;

import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.restaurant.voting.util.ValidationUtil.END_VOTING_TIME;

public class ExpiryUntilMidnight implements ExpiryPolicy {
    @Override
    public Duration getExpiryForCreation() {
        long msUntilMidnight = LocalTime.now().until(END_VOTING_TIME, ChronoUnit.MILLIS);
        return new Duration(TimeUnit.MILLISECONDS, msUntilMidnight);
    }

    @Override
    public Duration getExpiryForAccess() {
        return null;
    }

    @Override
    public Duration getExpiryForUpdate() {
        return null;
    }
}
