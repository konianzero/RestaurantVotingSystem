package org.restaurant.voting;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.to.VoteTo;

import static java.time.LocalDate.of;
import static org.restaurant.voting.RestaurantTestData.FIRST_RESTAURANT;
import static org.restaurant.voting.RestaurantTestData.SECOND_RESTAURANT;
import static org.restaurant.voting.UserTestData.ADMIN;
import static org.restaurant.voting.UserTestData.USER;
import static org.restaurant.voting.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    public static final TestMatcher<VoteTo> VOTE_TO_MATCHER = TestMatcher.usingEqualsComparator(VoteTo.class);

    public static final int NOT_FOUND = 1000;
    public static final int VOTE_1_ID = START_SEQ + 12;
    public static final int VOTE_2_ID = VOTE_1_ID + 1;

    public static final Vote VOTE_1 = new Vote(VOTE_1_ID, ADMIN, SECOND_RESTAURANT, of(2020, 12, 19));
    public static final Vote VOTE_2 = new Vote(VOTE_2_ID, USER, SECOND_RESTAURANT, of(2020, 12, 19));
    public static final Vote VOTE_3 = new Vote(VOTE_1_ID + 2, ADMIN, SECOND_RESTAURANT, of(2020, 12, 20));
    public static final Vote VOTE_4 = new Vote(VOTE_1_ID + 3, USER, FIRST_RESTAURANT, of(2020, 12, 20));

    public static final List<Vote> ALL_VOTES = List.of(VOTE_3, VOTE_4, VOTE_1, VOTE_2);

    public static Vote getNew() {
        return new Vote(null, ADMIN, FIRST_RESTAURANT, LocalDate.now());
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_1_ID, ADMIN, FIRST_RESTAURANT, LocalDate.now());
    }
}
