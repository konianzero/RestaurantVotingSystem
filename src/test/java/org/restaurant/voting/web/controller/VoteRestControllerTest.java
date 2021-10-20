package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.Test;
import org.restaurant.voting.TestUtil;
import org.restaurant.voting.model.Vote;
import org.restaurant.voting.repository.CrudVoteRepository;
import org.restaurant.voting.service.VoteService;
import org.restaurant.voting.to.VoteTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.restaurant.voting.UserTestData.ADMIN_EMAIL;
import static org.restaurant.voting.UserTestData.USER_EMAIL;
import static org.restaurant.voting.VoteTestData.*;
import static org.restaurant.voting.util.VoteUtil.createTo;
import static org.restaurant.voting.util.VoteUtil.getTos;
import static org.restaurant.voting.util.exception.ErrorType.DATA_NOT_FOUND;
import static org.restaurant.voting.util.exception.ErrorType.TIME_OVER;
import static org.restaurant.voting.util.validation.ValidationUtil.isVotingTimeOver;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + "/";

    @Autowired
    private VoteService service;
    @Autowired
    private CrudVoteRepository repository;

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void voteToday() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(newVote.getRestaurant().getId()))
                                      .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        VoteTo created = TestUtil.readFromJson(action, VoteTo.class);
        int newId = created.getId();
        newVote.setId(newId);

        VOTE_TO_MATCHER.assertMatch(created, createTo(newVote));
        VOTE_MATCHER.assertMatch(repository.findById(newId).get(), newVote);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getOwnVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(getTos(List.of(VOTE_2))));
    }

    @Test
    void getOwnVotesUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getLastVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/last"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(VOTE_3)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void revoteToday() throws Exception {
        Vote updated = getUpdated();
        if (isVotingTimeOver()) {
            revoteTodayAfter(updated.getRestaurant().getId());
        } else {
            revoteTodayBefore(updated);
        }
    }

    void revoteTodayAfter(int restaurantId) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(restaurantId))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(TIME_OVER));
    }

    void revoteTodayBefore(Vote updated) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(updated.getRestaurant().getId()))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Vote actual = repository.findById(VOTE_3_ID).get();
        VOTE_TO_MATCHER.assertMatch(createTo(actual), createTo(updated));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void voteTodayInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(1000))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void revoteTodayInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(1000))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }
}