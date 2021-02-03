package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.service.VoteService;
import org.restaurant.voting.to.VoteTo;
import org.restaurant.voting.util.JsonUtil;
import org.restaurant.voting.TestUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.restaurant.voting.TestUtil.userHttpBasic;
import static org.restaurant.voting.UserTestData.ADMIN;
import static org.restaurant.voting.UserTestData.ADMIN_ID;
import static org.restaurant.voting.UserTestData.USER;
import static org.restaurant.voting.UserTestData.USER_ID;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_MATCHER;
import static org.restaurant.voting.util.exception.ErrorType.VALIDATION_ERROR;
import static org.restaurant.voting.VoteTestData.*;
import static org.restaurant.voting.VoteTestData.NOT_FOUND;
import static org.restaurant.voting.util.ValidationUtil.isVotingTimeOver;
import static org.restaurant.voting.util.exception.ErrorType.TIME_OVER;
import static org.restaurant.voting.util.VoteUtil.createTo;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + "/";

    @Autowired
    private VoteService service;

    @Test
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        VoteTo newVoteTo = createTo(newVote);
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
                                      .with(userHttpBasic(USER))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newVoteTo))
        ).andExpect(status().isCreated());

        VoteTo created = TestUtil.readFromJson(action, VoteTo.class);
        int newId = created.getId();
        newVoteTo.setId(newId);
        newVote.setId(newId);

        VOTE_TO_MATCHER.assertMatch(created, newVoteTo);
        VOTE_MATCHER.assertMatch(service.get(newId, USER_ID), newVote);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_2_ID)
                                      .with(userHttpBasic(USER)))
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
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                                      .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getLastVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_3_ID)
                                      .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(VOTE_3)));
    }

    @Test
    void update() throws Exception {
        Vote updated = getUpdated();
        if (isVotingTimeOver.getAsBoolean()) {
            updateAfter(updated);
        } else {
            updateBefore(updated);
        }
    }

    void updateAfter(Vote updated) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_3_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(updated))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(TIME_OVER));
    }

    void updateBefore(Vote updated) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_3_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(updated))))
                .andExpect(status().isNoContent());

        Vote actual = service.get(VOTE_3_ID, ADMIN_ID);
        VOTE_MATCHER.assertMatch(actual, updated);
        RESTAURANT_MATCHER.assertMatch(actual.getRestaurant(), updated.getRestaurant());
    }

    @Test
    void createInvalid() throws Exception {
        VoteTo newVoteTo = new VoteTo(null, LocalDate.now(), ADMIN_ID, 1000);
        perform(MockMvcRequestBuilders.post(REST_URL)
                        .with(userHttpBasic(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(newVoteTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        VoteTo updateTo = new VoteTo(VOTE_1_ID, LocalDate.now(), ADMIN_ID, 1000);
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updateTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}