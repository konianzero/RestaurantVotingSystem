package org.restaurant.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.UserTestData.*;
import static org.restaurant.voting.VoteTestData.*;
import static org.restaurant.voting.VoteTestData.getUpdated;
import static org.restaurant.voting.VoteTestData.getNew;

public class VoteServiceTest extends AbstractServiceTest {
    @Autowired
    private VoteService service;

    @Test
    void get() {
        Vote actual = service.get(VOTE_1_ID, ADMIN_ID);
        VOTE_MATCHER.assertMatch(actual, VOTE_1);
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(VOTE_1_ID, USER_ID));
    }

    @Test
    void getAll() {
        VOTE_MATCHER.assertMatch(service.getAll(ADMIN_ID), VOTE_1, VOTE_3);
    }

    @Test
    void update() {
        Vote updated = getUpdated();
        service.update(updated, ADMIN_ID);
        VOTE_MATCHER.assertMatch(service.get(VOTE_3_ID, ADMIN_ID), getUpdated());
    }

    @Test
    void create() {
        Vote newVote = getNew();
        Vote created = service.create(newVote, USER_ID);
        int newId = created.id();
        newVote.setId(newId);
//        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(service.get(newId, USER_ID), newVote);
    }

    @Test
    void delete() {
        service.delete(VOTE_2_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(VOTE_2_ID, USER_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(VOTE_2_ID, ADMIN_ID));
    }
}