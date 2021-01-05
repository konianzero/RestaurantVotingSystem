package org.restaurant.voting.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.restaurant.voting.HasId;
import org.restaurant.voting.util.exception.IllegalRequestDataException;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.util.exception.VotingTimeOverException;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
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