package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.restaurant.voting.TestUtil;
import org.restaurant.voting.model.Vote;
import org.restaurant.voting.service.VoteService;
import org.restaurant.voting.to.VoteTo;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.restaurant.voting.UserTestData.*;
import static org.restaurant.voting.VoteTestData.NOT_FOUND;
import static org.restaurant.voting.VoteTestData.getNew;
import static org.restaurant.voting.VoteTestData.getUpdated;
import static org.restaurant.voting.VoteTestData.*;
import static org.restaurant.voting.util.VoteUtil.createTo;
import static org.restaurant.voting.util.exception.ErrorType.DATA_NOT_FOUND;
import static org.restaurant.voting.util.exception.ErrorType.TIME_OVER;
import static org.restaurant.voting.util.validation.ValidationUtil.isVotingTimeOver;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + "/";

    @Autowired
    private VoteService service;

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(
                MockMvcRequestBuilders.put(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(newVote.getRestaurant().getId()))
                                      .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        VoteTo created = TestUtil.readFromJson(action, VoteTo.class);
        int newId = created.getId();
        newVote.setId(newId);

        VOTE_TO_MATCHER.assertMatch(created, createTo(newVote));
        VOTE_MATCHER.assertMatch(service.get(newId, USER_ID), newVote);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_2_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(VOTE_2)));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_2_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getLastVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_3_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(VOTE_3)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Vote updated = getUpdated();
        if (isVotingTimeOver()) {
            updateAfter(updated.getRestaurant().getId());
        } else {
            updateBefore(updated);
        }
    }

    void updateAfter(int restaurantId) throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(restaurantId))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(TIME_OVER));
    }

    void updateBefore(Vote updated) throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(updated.getRestaurant().getId()))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Vote actual = service.get(VOTE_3_ID, ADMIN_ID);
        VOTE_TO_MATCHER.assertMatch(createTo(actual), createTo(updated));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(1000))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(1000))
                                      .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }
}