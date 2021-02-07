package org.restaurant.voting.util;

import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

import org.restaurant.voting.HasId;
import org.restaurant.voting.util.exception.ErrorType;
import org.restaurant.voting.util.exception.IllegalRequestDataException;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.util.exception.VotingTimeOverException;

import static org.restaurant.voting.web.converter.DateTimeFormatters.format;

public class ValidationUtil {
    public static final LocalTime END_VOTING_TIME =  LocalTime.of(23, 0);

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
        if (isVotingTimeOver()) {
            throw new VotingTimeOverException("It's " + format(LocalTime.now()) + ". Time for voting is over!");
        }
    }

    public static boolean isVotingTimeOver() {
        return LocalTime.now().isAfter(END_VOTING_TIME);
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