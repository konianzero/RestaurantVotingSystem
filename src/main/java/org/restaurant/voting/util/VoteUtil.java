package org.restaurant.voting.util;

import org.restaurant.voting.model.Vote;

import java.time.LocalDate;

public class VoteUtil {

    private VoteUtil() {
    }

    public static Vote createNew() {
        return new Vote(null, null, null, LocalDate.now());
    }
}
