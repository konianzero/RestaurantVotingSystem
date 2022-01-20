package org.restaurant.voting.service;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.restaurant.voting.model.Vote;
import org.restaurant.voting.repository.CrudVoteRepository;
import org.restaurant.voting.util.exception.VotingTimeOverException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.UserTestData.ADMIN_ID;
import static org.restaurant.voting.UserTestData.USER_ID;
import static org.restaurant.voting.VoteTestData.*;

class VoteServiceTest extends AbstractServiceTest {
    @Autowired
    private VoteService service;
    @Autowired
    private CrudVoteRepository repository;

    @Order(1)
    @Test
    void get() {
        List<Vote> actual = service.getAllByUserId(ADMIN_ID);
        VOTE_MATCHER.assertMatch(actual, VOTE_1, VOTE_3);
    }

    @Order(2)
    @Test
    void getLast() {
        Vote last = service.getLast(ADMIN_ID);
        VOTE_MATCHER.assertMatch(last, VOTE_3);
    }

    @Order(3)
    @Test
    void update() {
        Vote updated = getUpdated();
        if (service.isVotingTimeOver()) {
            assertThrows(VotingTimeOverException.class, () -> service.update(updated.getRestaurant().getId(), ADMIN_ID));
        } else {
            service.update(updated.getRestaurant().getId(), ADMIN_ID);
            VOTE_MATCHER.assertMatch(repository.findById(VOTE_3_ID).get(), updated);
        }
    }

    @Order(4)
    @Test
    void create() {
        Vote newVote = getNew();
        Vote created = service.create(newVote.getRestaurant().getId(), USER_ID);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.findById(newId).get(), newVote);
    }
}