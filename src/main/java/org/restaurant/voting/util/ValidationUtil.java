package org.restaurant.voting.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.util.exception.VotingTimeOverException;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean isFound, int id) {
        checkNotFound(isFound, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String message) {
        checkNotFound(object != null, message);
        return object;
    }

    public static void checkNotFound(boolean isFound, String message) {
        if (!isFound) {
            throw new NotFoundException("Not found entity with " + message);
        }
    }

    public static void checkTimeOver() {
        LocalTime now = LocalTime.now();
        LocalTime endTime = LocalTime.of(23, 0);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        if (now.isAfter(endTime)) {
            throw new VotingTimeOverException("It's " + timeFormatter.format(now) + ". Time for voting is over!");
        }
    }
}