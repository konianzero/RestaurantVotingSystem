package org.restaurant.voting;

import org.springframework.util.Assert;

public interface HasId {
    Integer getId();

    void setId(Integer id);

    default boolean isNew() {
        return getId() == null;
    }
}
