package org.restaurant.voting.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.restaurant.voting.HasId;
import org.restaurant.voting.util.exception.ErrorType;
import org.restaurant.voting.util.exception.IllegalRequestDataException;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.util.exception.VotingTimeOverException;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

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

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logStackTrace) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }
}