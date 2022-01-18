package org.restaurant.voting.util.validation;

import org.restaurant.voting.HasId;
import org.restaurant.voting.util.exception.IllegalRequestDataException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new EntityNotFoundException("Operation on entity with id=" + id + " not succeeded");
        }
    }

    public static <T> T checkNotFoundWithId(Optional<T> object, int id) {
        checkNotFound(object.isPresent(), "id=" + id);
        return object.get();
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
            throw new EntityNotFoundException("Not found entity with " + message);
        }
    }
}